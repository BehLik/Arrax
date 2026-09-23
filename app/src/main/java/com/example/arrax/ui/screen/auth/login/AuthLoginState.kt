package com.example.arrax.ui.screen.auth.login

data class AuthLoginState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val errorGeneral: String? = null
)
