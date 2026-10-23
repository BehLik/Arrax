package com.example.arrax.ui.screen.lobby.settings

import androidx.compose.runtime.Immutable
import com.example.arrax.core.model.ThemeMode
import com.example.arrax.domain.model.AlertChannel
import com.example.arrax.domain.model.WeightUnit

@Immutable
data class LobbySettingsState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val weightUnit: WeightUnit = WeightUnit.KG,
    val alerts: Map<AlertChannel, Boolean> = AlertChannel.entries.associateWith { true }
)