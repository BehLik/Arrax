package com.example.arrax.ui.screen.lobby.join

import com.example.arrax.core.model.JoinMethod

data class LobbyJoinState(
    val method: JoinMethod = JoinMethod.CODE,
    val code: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean=false,
    val errorMessage: String? = null,
    val torchOn: Boolean = false,
)

sealed interface LobbyJoinEvent {
    data class Joined(val tenantId: String) : LobbyJoinEvent
}