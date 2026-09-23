package com.example.arrax.ui.root.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.navigation.Routes
import com.example.arrax.ui.root.AppStartViewmodel
import com.example.arrax.ui.screen.auth.forgotpassword.AuthForGotPasswordScreen
import com.example.arrax.ui.screen.auth.login.AuthLoginScreen
import com.example.arrax.ui.screen.auth.register.AuthRegisterScreen
import com.example.arrax.ui.screen.lobby.home.LobbyHomeScreen
import com.example.arrax.ui.screen.lobby.onboarding.LobbyOnboardingScreen

@Composable
fun ArraxNavHost(
    navController: NavHostController = rememberNavController(),
    appStartViewmodel: AppStartViewmodel = hiltViewModel()
) {
    val destinoInicial by appStartViewmodel.destinoInicial.collectAsState()
    val startDestination = destinoInicial

    if (startDestination == null) {
        ArxLoading(message = "Cargando...")
        return
    }

    NavHost(navController = navController, startDestination = startDestination.route) {
        composable(Routes.Login.route) {
            AuthLoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.LobbyOnboarding.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Routes.Register.route) },
                onNavigateToForgotPassword = { navController.navigate(Routes.ForgotPassword.route) }
            )
        }
        composable(Routes.Register.route) {
            AuthRegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.LobbyOnboarding.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable(Routes.ForgotPassword.route) {
            AuthForGotPasswordScreen(onNavigateBackToLogin = { navController.popBackStack() })
        }
        composable(Routes.LobbyOnboarding.route) {
            LobbyOnboardingScreen(
                onOnboardingCompletado = {
                    navController.navigate(Routes.LobbyHome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.LobbyHome.route) {
            LobbyHomeScreen(
                onEventoClick = { eventoId ->
                    // Fase 3: cambiar por Routes.EventDetail con navArgument real;
                    // por ahora se arma el literal a mano porque la ruta no está montada aún.
                    navController.navigate("evento/detail/$eventoId")
                },
                onNuevoEventoClick = { navController.navigate(Routes.EventCreate.route) }
            )
        }
        // EventCreate / EventDetail: Fase 3
    }
}