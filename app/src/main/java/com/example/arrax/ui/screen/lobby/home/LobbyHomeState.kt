package com.example.arrax.ui.screen.lobby.home

data class NegocioResumen(
    val id: String,
    val nombre: String,
    val esPropietario: Boolean,
    val totalEventos: Int
)

data class LobbyHomeState(
    val nombreUsuario: String = "",
    val negociosRecientes: List<NegocioResumen> = emptyList(),
    val notificacionesSinLeer: Int = 0,
    val isLoading: Boolean = true,
    val estaSinConexion: Boolean = false
)