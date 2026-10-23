package com.example.arrax.domain.usecase.user

import com.example.arrax.core.model.ThemeMode
import com.example.arrax.domain.model.AlertChannel
import com.example.arrax.domain.model.UserPreferences
import com.example.arrax.domain.model.WeightUnit
import com.example.arrax.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePreferencesUseCase @Inject constructor(private val repo: PreferencesRepository) {
    operator fun invoke(): Flow<UserPreferences> = repo.observe()
}

class UpdateThemeModeUseCase @Inject constructor(private val repo: PreferencesRepository) {
    operator fun invoke(mode: ThemeMode) = repo.setThemeMode(mode)
}

class UpdateWeightUnitUseCase @Inject constructor(private val repo: PreferencesRepository) {
    operator fun invoke(unit: WeightUnit) = repo.setWeightUnit(unit)
}

class SetAlertEnabledUseCase @Inject constructor(private val repo: PreferencesRepository) {
    operator fun invoke(channel: AlertChannel, enabled: Boolean) = repo.setAlertEnabled(channel, enabled)
}