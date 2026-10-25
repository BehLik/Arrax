package com.example.arrax.domain.model



enum class WeightUnit { KG, LB }

enum class AlertChannel { NEW_ORDERS, UPCOMING_EVENTS, CONFIRMATIONS, PROMOTIONS, SYSTEM }

data class UserPreferences(
    val themeMode: ArxMode = ThemeMode.SYSTEM,
    val weightUnit: WeightUnit = WeightUnit.KG,
    val alerts: Map<AlertChannel, Boolean> = AlertChannel.entries.associateWith { true }
)