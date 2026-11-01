package com.example.arrax.ui.screen.lobby.profile.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.NetworkMonitor
import com.example.arrax.core.model.ThemeMode
import com.example.arrax.domain.model.AlertChannel
import com.example.arrax.domain.model.WeightUnit
import com.example.arrax.domain.usecase.user.ObservePreferencesUseCase
import com.example.arrax.domain.usecase.user.SetAlertEnabledUseCase
import com.example.arrax.domain.usecase.user.SetNotificationsEnabledUseCase
import com.example.arrax.domain.usecase.user.UpdateThemeModeUseCase
import com.example.arrax.domain.usecase.user.UpdateWeightUnitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LobbySettingsViewmodel @Inject constructor(
    observePreferences: ObservePreferencesUseCase,
    networkMonitor: NetworkMonitor,
    private val updateThemeMode: UpdateThemeModeUseCase,
    private val updateWeightUnit: UpdateWeightUnitUseCase,
    private val setAlertEnabled: SetAlertEnabledUseCase,
    private val setNotificationsEnabled: SetNotificationsEnabledUseCase
) : ViewModel() {

    val state: StateFlow<LobbySettingsState> = observePreferences()
        .map {
            LobbySettingsState(
                themeMode = it.themeMode,
                weightUnit = it.weightUnit,
                alerts = it.alerts,
                notificationsEnabled = it.notificationsEnabled
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LobbySettingsState())

    fun onThemeModeChange(mode: ThemeMode) = updateThemeMode(mode)
    fun onWeightUnitChange(unit: WeightUnit) = updateWeightUnit(unit)

    fun onNotificationsToggle(enabled: Boolean) {
        if (enabled && state.value.alerts.none { it.value }) {
            AlertChannel.entries.forEach { setAlertEnabled(it, true) }
        }
        setNotificationsEnabled(enabled)
    }

    fun onAlertToggle(channel: AlertChannel, enabled: Boolean) {
        setAlertEnabled(channel, enabled)
        val anyOn = AlertChannel.entries.any {
            if (it == channel) enabled else state.value.alerts[it] ?: true
        }
        if (!anyOn) setNotificationsEnabled(false)
    }
}