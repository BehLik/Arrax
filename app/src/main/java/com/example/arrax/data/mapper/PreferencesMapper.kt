package com.example.arrax.data.mapper

import com.example.arrax.core.model.ThemeMode
import com.example.arrax.domain.model.AlertChannel
import com.example.arrax.domain.model.UserPreferences
import com.example.arrax.domain.model.WeightUnit

object PreferencesKeys {
    const val THEME_MODE = "themeMode"
    const val WEIGHT_UNIT = "weightUnit"
    const val ALERTS = "alerts"
}

/** Tolerante: campos ausentes o con valores desconocidos caen al valor por defecto. */
fun Map<String, Any?>?.toUserPreferences(): UserPreferences {
    val defaults = UserPreferences()
    if (this == null) return defaults

    val theme = (this[PreferencesKeys.THEME_MODE] as? String)
        ?.let { runCatching { ThemeMode.valueOf(it) }.getOrNull() }
        ?: defaults.themeMode

    val unit = (this[PreferencesKeys.WEIGHT_UNIT] as? String)
        ?.let { runCatching { WeightUnit.valueOf(it) }.getOrNull() }
        ?: defaults.weightUnit

    @Suppress("UNCHECKED_CAST")
    val rawAlerts = this[PreferencesKeys.ALERTS] as? Map<String, Any?>
    val alerts = AlertChannel.entries.associateWith { channel ->
        (rawAlerts?.get(channel.name) as? Boolean) ?: true
    }

    return UserPreferences(theme, unit, alerts)
}