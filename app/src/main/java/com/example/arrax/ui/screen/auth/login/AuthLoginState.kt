package com.example.arrax.ui.screen.auth.login

data class AuthLoginState (
    val email: String="",
    val password: String="",
    val isLoading: Boolean=false,
    val errorMessage: String?=null
)