package com.example.arrax.ui.screen.lobby.home

import com.example.arrax.domain.model.Event

data class LobbyHomeState(
    val eventos: List<Event> = emptyList(),
    val isLoading: Boolean = true,
    val esAdmin: Boolean = false,
    val estaSinConexion: Boolean = false
)