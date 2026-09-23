package com.example.arrax.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Cachea tenantId + rol en DataStore. Es lo que permite saber a qué tenant
 * hacer scoping de las queries de Firestore desde el arranque, incluso sin red,
 * antes de que /usuarios/{uid} confirme el dato en segundo plano.
 */
@Singleton
class SesionLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val tenantIdKey = stringPreferencesKey("tenant_id")
    private val roleKey = stringPreferencesKey("role")

    val tenantId: Flow<String?> = dataStore.data.map { it[tenantIdKey] }
    val role: Flow<String?> = dataStore.data.map { it[roleKey] }

    suspend fun saveSession(tenantId: String, role: String) {
        dataStore.edit { prefs ->
            prefs[tenantIdKey] = tenantId
            prefs[roleKey] = role
        }
    }

    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }
}
