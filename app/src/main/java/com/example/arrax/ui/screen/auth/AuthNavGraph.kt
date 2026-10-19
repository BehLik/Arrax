package com.example.arrax.ui.screen.auth

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.arrax.core.navigation.*
import com.example.arrax.ui.root.navigation.PlaceholderScreen
import com.example.arrax.ui.root.navigation.backAction
import com.example.arrax.ui.root.navigation.safeNavigate
import com.example.arrax.ui.screen.auth.forgotpassword.ForgotPasswordScreen
import com.example.arrax.ui.screen.auth.login.AuthLoginScreen

fun NavGraphBuilder.authGraph(nav: NavController) {
    val back = nav.backAction()

    navigation<AuthGraph>(startDestination = Login) {

        composable<Login>(
            enterTransition = { fadeIn(tween(400)) },
            popExitTransition = { fadeOut(tween(400)) }
        ) {
            AuthLoginScreen(

                onLoginSuccess = {
                    nav.safeNavigate(LobbyGraph) { popUpTo<AuthGraph> { inclusive = true } }
                },
                onNavigateToRegister = { nav.safeNavigate(Register) },
                onNavigateToForgotPassword = { nav.safeNavigate(ForgotPassword) }
            )
        }

        composable<Register> {
            // TODO: reemplazar por AuthRegisterScreen(...). Al registrarse con éxito:
            //   nav.safeNavigate(LobbyGraph) { popUpTo<AuthGraph> { inclusive = true } }
            PlaceholderScreen(title = "Registro", onBack = back)
        }

        composable<ForgotPassword> {
            ForgotPasswordScreen(
                onNavigateBackToLogin = back,
                onNavigateBack = back
            )
        }
    }
}