package com.example.arrax.ui.screen.lobby.profile.changepassword

data class LobbyChangePasswordState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val currentPasswordError: String? = null,
    val newPasswordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorGeneral: String? = null
) {
    val canSubmit: Boolean
        get() = currentPassword.isNotBlank() && newPassword.isNotBlank() && confirmPassword.isNotBlank()
}