package com.example.arrax.ui.screen.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.arrax.core.navigation.AuthGraph
import com.example.arrax.core.navigation.ForgotPassword
import com.example.arrax.core.navigation.LobbyGraph
import com.example.arrax.core.navigation.LobbyHome
import com.example.arrax.core.navigation.Login
import com.example.arrax.core.navigation.Register
import com.example.arrax.ui.screen.auth.forgotpassword.ForgotPasswordScreen
import com.example.arrax.ui.screen.auth.login.AuthLoginScreen

fun NavGraphBuilder.authGraph(nav: NavController) {
    navigation<AuthGraph>(startDestination = Login) {
        composable<Login> {
            AuthLoginScreen(
                onLoginSuccess = {
                    nav.navigate(LobbyGraph) {
                        popUpTo<AuthGraph> { inclusive = true }
                    }
                },
                onNavigateToRegister = { nav.navigate(Register) },
                onNavigateToForgotPassword = { nav.navigate(ForgotPassword) }
            )
        }
    composable<Register> { /* AuthRegisterScreen(...) */ }
    composable<ForgotPassword> {
        ForgotPasswordScreen(
            onNavigateBackToLogin = { nav.popBackStack() },
            onNavigateBack = { nav.popBackStack() }
        )
    }
}
}