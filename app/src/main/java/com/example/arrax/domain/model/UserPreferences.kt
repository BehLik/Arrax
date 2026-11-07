package com.example.arrax.domain.model

import com.example.arrax.domain.model.ThemeMode

enum class WeightUnit { KG, LB }

enum class AlertChannel { NEW_ORDERS, OPERATOR_JOINED, UPCOMING_EVENTS, CONFIRMATIONS, PROMOTIONS, SYSTEM }

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val weightUnit: WeightUnit = WeightUnit.KG,
    val alerts: Map<AlertChannel, Boolean> = AlertChannel.entries.associateWith { true },
    val notificationsEnabled: Boolean=false,
    val notificationsDecided:Boolean=false
)