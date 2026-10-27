package com.example.arrax.ui.screen.lobby.home

import androidx.compose.runtime.Immutable

@Immutable
data class BusinessItem(
    val id: String,
    val name: String,
    val isAdmin: Boolean,
    val totalEvents: Int
)

@Immutable
data class LobbyHomeState(
    val userName: String = "",
    val recentBusinesses: List<BusinessItem> = emptyList(),
    val hasMoreBusinesses: Boolean = false,
    val unreadNotifications: Int = 0,
    val isLoading: Boolean = true,
    val isOffline: Boolean = false,
    val isRefreshing:Boolean=false
)
