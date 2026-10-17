package com.example.arrax.ui.root.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.arrax.core.navigation.AuthGraph
import com.example.arrax.core.navigation.ForgotPassword
import com.example.arrax.core.navigation.LobbyGraph
import com.example.arrax.core.navigation.Login
import com.example.arrax.core.navigation.Register
import com.example.arrax.ui.screen.auth.login.AuthLoginScreen

fun NavGraphBuilder.authGraph(nav: NavController) {
    navigation<AuthGraph>(startDestination = Login) {
        composable<Login> {
            AuthLoginScreen(
                onLoggedIn = {
                    nav.navigate(LobbyGraph) {
                        popUpTo<AuthGraph> { inclusive = true }
                    }
                },
                onRegisterClick = { nav.navigate(Register) },
                onForgotClick = { nav.navigate(ForgotPassword) }
            )
        }
        composable<Register> { /* ... */ }
        composable<ForgotPassword> { /* ... */ }
    }
}