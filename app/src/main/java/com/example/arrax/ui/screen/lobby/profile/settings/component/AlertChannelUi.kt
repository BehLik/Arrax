package com.example.arrax.ui.screen.lobby.profile.settings.component

import androidx.annotation.StringRes
import com.example.arrax.R
import com.example.arrax.domain.model.AlertChannel

@get:StringRes
val AlertChannel.titleRes: Int
    get() = when (this) {
        AlertChannel.NEW_ORDERS -> R.string.settings_alert_new_orders_title
        AlertChannel.UPCOMING_EVENTS -> R.string.settings_alert_upcoming_events_title
        AlertChannel.CONFIRMATIONS -> R.string.settings_alert_confirmations_title
        AlertChannel.PROMOTIONS -> R.string.settings_alert_promotions_title
        AlertChannel.SYSTEM -> R.string.settings_alert_system_title
    }

@get:StringRes
val AlertChannel.subtitleRes: Int
    get() = when (this) {
        AlertChannel.NEW_ORDERS -> R.string.settings_alert_new_orders_subtitle
        AlertChannel.UPCOMING_EVENTS -> R.string.settings_alert_upcoming_events_subtitle
        AlertChannel.CONFIRMATIONS -> R.string.settings_alert_confirmations_subtitle
        AlertChannel.PROMOTIONS -> R.string.settings_alert_promotions_subtitle
        AlertChannel.SYSTEM -> R.string.settings_alert_system_subtitle
    }