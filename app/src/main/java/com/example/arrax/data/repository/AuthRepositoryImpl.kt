package com.example.arrax.data.repository


import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.Resource
import com.example.arrax.data.remote.FirebaseAuthDataSource
import com.example.arrax.data.remote.FirestoreTenantDataSource
import com.example.arrax.domain.model.Role
import com.example.arrax.domain.model.User
import com.example.arrax.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: FirebaseAuthDataSource,
    private val tenantDataSource: FirestoreTenantDataSource
) : AuthRepository {

    override fun observeCurrentUser(): Flow<User?> =
        authDataSource.observeAuthState().flatMapLatest { firebaseUser ->
            if (firebaseUser == null) {
                flowOf(null)
            } else {
                tenantDataSource.observeUser(firebaseUser.uid).map { dto ->
                    dto?.let {
                        User(
                            uid = it.uid,
                            name = it.name,
                            tenantId = it.tenantId,
                            role = runCatching { Role.valueOf(it.role) }.getOrDefault(Role.OPERATOR)
                        )
                    } ?: User(uid = firebaseUser.uid)
                }
            }
        }

    override suspend fun login(email: String, password: String): Resource<User> = try {
        val firebaseUser = authDataSource.login(email, password)
        if (!authDataSource.isCurrentEmailVerified()) {
            // No se cierra sesión: VerifyEmail la necesita para reenviar/comprobar.
            val error = AppError.EmailNotVerified()
            Resource.Error(error.message ?: "Correo no verificado", error)
        } else {
            Resource.Success(User(uid = firebaseUser.uid, name = firebaseUser.displayName.orEmpty()))
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al iniciar sesión", e)
    }

    override suspend fun register(email: String, password: String, name: String): Resource<User> = try {
        val firebaseUser = authDataSource.register(email, password)
        authDataSource.sendEmailVerification()
        Resource.Success(User(uid = firebaseUser.uid, name = name))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al registrar", e)
    }

    override suspend fun sendEmailVerification(): Resource<Unit> = try {
        authDataSource.sendEmailVerification()
        Resource.Success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al enviar el correo de verificación", e)
    }

    override suspend fun checkEmailVerified(): Resource<Boolean> = try {
        Resource.Success(authDataSource.reloadAndCheckVerified())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al comprobar la verificación", e)
    }

    override suspend fun cancelUnverifiedRegistration(): Resource<Unit> = try {
        authDataSource.deleteCurrentUser()
        Resource.Success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        // Si falla el borrado (p. ej. sesión antigua), al menos cerramos sesión.
        authDataSource.logout()
        Resource.Error(e.message ?: "Error al cancelar el registro", e)
    }

    override fun currentEmail(): String? = authDataSource.currentEmail()
    override fun isEmailVerified(): Boolean = authDataSource.isCurrentEmailVerified()
    override suspend fun sendPasswordReset(email: String): Resource<Unit> = try {
        authDataSource.sendPasswordReset(email)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al enviar correo de recuperación", e)
    }

    override suspend fun logout() = authDataSource.logout()
}
