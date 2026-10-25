package com.example.arrax.ui.screen.business.home

import androidx.annotation.DrawableRes
import com.example.arrax.core.designsystem.components.display.ArxTone

data class HomeNextEventUi(val whenLabel: String, val message: String)

data class HomeAlertUi(
    val id: String,
    val title: String,
    val detail: String,
    val statusLabel: String,
    val tone: ArxTone,
    @DrawableRes val icon: Int
)

data class HomeSalesSliceUi(
    val label: String,
    val percentLabel: String,
    val fraction: Float,
    val tone: ArxTone
)

data class BusinessHomeState(
    val isLoading: Boolean = false,
    val businessName: String = "",
    val roleLabel: String = "",
    val memberCount: Int = 0,
    val unreadNotifications: Int = 0,
    val upcomingEventsCount: Int = 0,
    val customersCount: Int = 0,
    val nextEvent: HomeNextEventUi? = null,
    val alerts: List<HomeAlertUi> = emptyList(),
    val salesTotal: String = "",
    val salesBreakdown: List<HomeSalesSliceUi> = emptyList()
)