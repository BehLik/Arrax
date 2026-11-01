package com.example.arrax.data.local

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class NotificationPromptLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val key = longPreferencesKey("notif_prompt_snoozed_until")
    val snoozedUntil: Flow<Long> = dataStore.data.map { it[key] ?: 0L }

    suspend fun snooze(days: Long = 7) {
        dataStore.edit { it[key] = System.currentTimeMillis() + days * 86_400_000L }
    }
}