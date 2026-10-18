package com.example.arrax.ui.root.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.ui.root.AppStartViewModel
import com.example.arrax.ui.screen.auth.authGraph
import com.example.arrax.ui.screen.business.businessGraph
import com.example.arrax.ui.screen.events.eventGraph
import com.example.arrax.ui.screen.lobby.lobbyGraph

// MainActivity calls ArraxNavHost() with no arguments, so it owns the controller
// and resolves the start destination itself.
@Composable
fun ArraxNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: AppStartViewModel = hiltViewModel()
) {
    val startDestination by viewModel.startDestination.collectAsState()
    val start = startDestination

    if (start == null) {
        ArxLoading()
    } else {
        NavHost(navController = navController, startDestination = start) {
            authGraph(navController)
            lobbyGraph(navController)
            businessGraph(navController)
            eventGraph(navController)
        }
    }
}