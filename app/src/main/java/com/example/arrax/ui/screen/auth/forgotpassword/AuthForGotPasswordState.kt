package com.example.arrax.ui.screen.auth.forgotpassword

data class AuthForGotPasswordState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val isSent: Boolean = false,
    val errorGeneral: String? = null
)
