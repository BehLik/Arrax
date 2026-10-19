package com.example.arrax.ui.root.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.navigation.ForgotPassword
import com.example.arrax.core.navigation.LobbyHome
import com.example.arrax.core.navigation.Login
import com.example.arrax.core.navigation.Register
import com.example.arrax.ui.root.AppStartViewModel
import com.example.arrax.ui.screen.auth.authGraph
import com.example.arrax.ui.screen.business.businessGraph
import com.example.arrax.ui.screen.events.eventGraph
import com.example.arrax.ui.screen.lobby.lobbyGraph
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackdrop
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackdropHost
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackdropState
//import com.example.arrax.core.designsystem.components.backgrounds.LocalArxBackdropHoisted
import com.example.arrax.core.designsystem.icons.ArxIcons.Box

// MainActivity calls ArraxNavHost() with no arguments, so it owns the controller
// and resolves the start destination itself.
//private data class Backdrop(val decoration: ArxDecoration, val invert: Boolean = false)
//
///** Aquí listas las pantallas que llevan montañas. Ajusta a tu diseño. */
//private fun NavDestination?.backdrop(): Backdrop = when {
//    this == null -> Backdrop(ArxDecoration.None)
//    hasRoute<Login>() || hasRoute<Register>() ||
//            hasRoute<ForgotPassword>() || hasRoute<LobbyHome>() ->
//        Backdrop(ArxDecoration.Mountains)   // si alguna usa invertMountains = true, ponlo aquí
//    else -> Backdrop(ArxDecoration.None)
//}

@Composable
fun ArraxNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: AppStartViewModel = hiltViewModel()
) {
    val startDestination by viewModel.startDestination.collectAsState()
    val start = startDestination
    val backdropState = remember { ArxBackdropState() }

    ArxBackdropHost(state = backdropState) {
        if (start == null) {
            ArxLoading()
        } else {
            NavHost(
                navController = navController,
                startDestination = start,
                enterTransition = ArraxTransitions.enter,
                exitTransition = ArraxTransitions.exit,
                popEnterTransition = ArraxTransitions.popEnter,
                popExitTransition = ArraxTransitions.popExit
            ) {
                authGraph(navController)
                lobbyGraph(navController)
                businessGraph(navController)
                eventGraph(navController)
            }
        }
    }
}