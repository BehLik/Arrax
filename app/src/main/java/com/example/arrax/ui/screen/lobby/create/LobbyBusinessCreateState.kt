package com.example.arrax.ui.screen.lobby.create

data class LobbyBusinessCreateState(
    val name: String = "",
    val isLoading: Boolean = false,
    val nameError: String? = null,
    val error: String? = null
) {
    val canSubmit: Boolean get() = name.isNotBlank() && !isLoading
}