package com.example.arrax.ui.screen.auth.verifyemail

data class VerifyEmailState(
    val email: String = "",
    val isChecking: Boolean = false,
    val isResending: Boolean = false,
    val resendCooldown: Int = 0,
    val infoMessage: String? = null,
    val errorGeneral: String? = null
)