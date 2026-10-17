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
// ui/root/navigation/ArraxNavHost.kt
@Composable
fun ArraxNavHost(navController: NavHostController, startDestination: Any) {
    NavHost(navController, startDestination = startDestination) {
        authGraph(navController)
        lobbyGraph(navController)
        businessGraph(navController)
    }
}