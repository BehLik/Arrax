package com.example.arrax.ui.root.startup

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.arrax.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

// ─────────────────────────────────────────────
// 1) A dónde puede arrancar la app
// ─────────────────────────────────────────────
sealed interface StartTarget {
    data object Auth : StartTarget
    data object VerifyEmail : StartTarget
    data object Lobby : StartTarget
    data class Business(val businessId: String) : StartTarget
    data class Event(val businessId: String, val eventId: String) : StartTarget
}

// ─────────────────────────────────────────────
// 2) La "llave del casillero": solo IDs, nunca pantallas
// ─────────────────────────────────────────────
data class LastContext(val businessId: String, val eventId: String?)

private val Context.lastContextDataStore by preferencesDataStore("arrax_last_context")

@Singleton
class LastContextStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val businessKey = stringPreferencesKey("business_id")
    private val eventKey = stringPreferencesKey("event_id")

    suspend fun read(): LastContext? {
        val prefs = context.lastContextDataStore.data.first()
        val business = prefs[businessKey] ?: return null
        return LastContext(business, prefs[eventKey])
    }

    suspend fun save(businessId: String, eventId: String?) {
        context.lastContextDataStore.edit {
            it[businessKey] = businessId
            if (eventId == null) it.remove(eventKey) else it[eventKey] = eventId
        }
    }

    suspend fun clear() {
        context.lastContextDataStore.edit { it.clear() }
    }
}

// ─────────────────────────────────────────────
// 3) Validación del contexto guardado
//    true  = sigue válido
//    false = ya no sirve (evento cerrado, sin acceso...)
//    null  = no se pudo verificar (sin internet) -> confiamos
// ─────────────────────────────────────────────
interface ContextValidator {
    suspend fun canAccessBusiness(businessId: String): Boolean?
    suspend fun isEventOpen(businessId: String, eventId: String): Boolean?
}

/**
 * Versión temporal para que compile: confía siempre.
 * No es peligroso: ProvideAccess ya saca al usuario si no tiene acceso.
 * Reemplázala por una que consulte tus repositorios.
 */
class OptimisticContextValidator @Inject constructor() : ContextValidator {
    override suspend fun canAccessBusiness(businessId: String): Boolean? = null
    override suspend fun isEventOpen(businessId: String, eventId: String): Boolean? = null
}

@Module
@InstallIn(SingletonComponent::class)
abstract class StartupModule {
    @Binds
    abstract fun bindValidator(impl: OptimisticContextValidator): ContextValidator
}

// ─────────────────────────────────────────────
// 4) El resolver: decide el destino de arranque
// ─────────────────────────────────────────────
@Singleton
class StartupResolver @Inject constructor(
    private val authRepository: AuthRepository,
    private val store: LastContextStore,
    private val validator: ContextValidator,
) {
    suspend fun resolve(): StartTarget {
        val user = authRepository.observeCurrentUser().first()
            ?: return StartTarget.Auth
        if (!authRepository.isEmailVerified()) return StartTarget.VerifyEmail

        val saved = store.read() ?: return StartTarget.Lobby

        // 1) ¿Puede reanudar en el evento?
        if (saved.eventId != null) {
            val eventOk = validator.isEventOpen(saved.businessId, saved.eventId)
            if (eventOk != false) return StartTarget.Event(saved.businessId, saved.eventId)
        }

        // 2) Si el evento ya no sirve, cae al negocio
        return when (validator.canAccessBusiness(saved.businessId)) {
            false -> {
                store.clear()
                StartTarget.Lobby
            }
            else -> StartTarget.Business(saved.businessId)
        }
    }
}