package com.example.arrax.ui.screen.business.alerts

import androidx.annotation.DrawableRes
import com.example.arrax.ui.screen.business.BusinessTone

enum class AlertsTab { All, Events, Invitations, Orders }

data class AlertUi(
    val id: String,
    val category: AlertsTab,
    val title: String,
    val detail: String,
    val supporting: String? = null,
    @DrawableRes val icon: Int,
    val tone: BusinessTone,
    val isUnread: Boolean = false
)

data class BusinessAlertsState(
    val isLoading: Boolean = false,
    val selectedTab: AlertsTab = AlertsTab.All,
    val alerts: List<AlertUi> = emptyList()
)