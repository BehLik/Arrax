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
import com.example.arrax.ui.screen.auth.register.AuthRegisterScreen
import com.example.arrax.ui.screen.auth.verifyemail.VerifyEmailScreen

fun NavGraphBuilder.authGraph(nav: NavController, startOnVerify: Boolean = false) {
    val back = nav.backAction()

    navigation<AuthGraph>(startDestination = if (startOnVerify) VerifyEmail else Login) {

        composable<Login>(
            enterTransition = { fadeIn(tween(400)) },
            popExitTransition = { fadeOut(tween(400)) }
        ) {
            AuthLoginScreen(
                onLoginSuccess = {
                    nav.safeNavigate(LobbyGraph) { popUpTo<AuthGraph> { inclusive = true } }
                },
                onNavigateToRegister = { nav.safeNavigate(Register) },
                onNavigateToForgotPassword = { nav.safeNavigate(ForgotPassword) },
                onNavigateToVerifyEmail = { nav.safeNavigate(VerifyEmail) }
            )
        }

        composable<Register> {
            AuthRegisterScreen(
                onRegisterSuccess = { nav.safeNavigate(VerifyEmail) },
                onNavigateBack = back,
                onNavigateToLogin = { nav.safeNavigate(Login) }
            )
        }

        composable<VerifyEmail> {
            VerifyEmailScreen(
                onVerified = { nav.safeNavigate(LobbyGraph) { popUpTo<AuthGraph> { inclusive = true } } },
                onCancel = { nav.safeNavigate(Login) { popUpTo<Login> { inclusive = true } } }
            )
        }

        composable<ForgotPassword> {
            ForgotPasswordScreen(
                onNavigateBackToLogin = back,
                onNavigateBack = back
            )
        }
    }
}