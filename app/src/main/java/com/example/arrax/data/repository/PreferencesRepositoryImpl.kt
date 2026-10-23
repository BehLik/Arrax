package com.example.arrax.data.repository

import com.example.arrax.core.model.ThemeMode
import com.example.arrax.data.mapper.PreferencesKeys
import com.example.arrax.data.mapper.toUserPreferences
import com.example.arrax.data.remote.FirestorePreferencesDataSource
import com.example.arrax.domain.model.AlertChannel
import com.example.arrax.domain.model.UserPreferences
import com.example.arrax.domain.model.WeightUnit
import com.example.arrax.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val remote: FirestorePreferencesDataSource
) : PreferencesRepository {

    override fun observe(): Flow<UserPreferences> =
        remote.observe()
            .map { it.toUserPreferences() }
            .catch { emit(UserPreferences()) } // ej. permisos: la UI sigue con valores por defecto

    override fun setThemeMode(mode: ThemeMode) =
        remote.merge(mapOf(PreferencesKeys.THEME_MODE to mode.name))

    override fun setWeightUnit(unit: WeightUnit) =
        remote.merge(mapOf(PreferencesKeys.WEIGHT_UNIT to unit.name))

    override fun setAlertEnabled(channel: AlertChannel, enabled: Boolean) =
        remote.merge(mapOf(PreferencesKeys.ALERTS to mapOf(channel.name to enabled)))
}