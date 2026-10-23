package com.example.arrax.domain.repository

import com.example.arrax.core.model.ThemeMode
import com.example.arrax.domain.model.AlertChannel
import com.example.arrax.domain.model.UserPreferences
import com.example.arrax.domain.model.WeightUnit
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun observe(): Flow<UserPreferences>

    // No son suspend a propósito: offline la confirmación del servidor tarda; la caché local ya emite el cambio.
    fun setThemeMode(mode: ThemeMode)
    fun setWeightUnit(unit: WeightUnit)
    fun setAlertEnabled(channel: AlertChannel, enabled: Boolean)
}