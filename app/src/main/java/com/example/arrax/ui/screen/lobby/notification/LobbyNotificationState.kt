package com.example.arrax.ui.screen.lobby.notification


data class LobbyNotificationState(
    val isLoading: Boolean = true,
    val notifications: List<NotificationUi> = emptyList(),
    val error: String? = null
)
data class NotificationUi(
    val id: String,
    val title: String,
    val message: String,
    val timeLabel: String,
    val isRead: Boolean
)