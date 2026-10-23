package com.example.arrax.ui.screen.lobby.profile.personaldata

data class LobbyPersonalDataState(
    val name: String = "",
    val originalName: String = "",
    val email: String = "",
    val isGoogleAccount: Boolean = false,
    val isLoaded: Boolean = false,
    val nameError: String? = null,
    val errorGeneral: String? = null
) {
    /** Hay algo que guardar: ya cargó, el nombre no está vacío y cambió. */
    val canSave: Boolean
        get() = isLoaded && name.isNotBlank() && name.trim() != originalName
}