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
 * Caches tenantId + role in DataStore. It lets us scope Firestore queries to the right tenant
 * from startup, even offline, before /users/{uid} confirms the data in the background.
 * UI hint only: real authorization is enforced by firestore.rules.
 */
@Singleton
class SessionLocalDataSource @Inject constructor(
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
