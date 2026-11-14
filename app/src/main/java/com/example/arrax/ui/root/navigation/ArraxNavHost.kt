package com.example.arrax.ui.root.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackdropHost
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackdropState
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.navigation.BusinessEvents
import com.example.arrax.core.navigation.BusinessHome
import com.example.arrax.core.navigation.EventShell
import com.example.arrax.core.navigation.LobbyHome
import com.example.arrax.core.navigation.Login
import com.example.arrax.ui.root.AppStartViewModel
import com.example.arrax.ui.root.session.CurrentUserViewModel
import com.example.arrax.ui.root.session.LocalCurrentUser
import com.example.arrax.ui.root.startup.StartTarget
import com.example.arrax.ui.screen.auth.authGraph
import com.example.arrax.ui.screen.business.businessGraph
import com.example.arrax.ui.screen.lobby.lobbyGraph

@Composable
fun ArraxNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: AppStartViewModel = hiltViewModel(),
    currentUserViewModel: CurrentUserViewModel = hiltViewModel()
) {
    val start by viewModel.start.collectAsState()
    val current = start
    val currentUser by currentUserViewModel.user.collectAsState()
    val backdropState = remember { ArxBackdropState() }

    CompositionLocalProvider(LocalCurrentUser provides currentUser) {
        ArxBackdropHost(state = backdropState) {
            if (current == null) {
                ArxLoading()
            } else {
                // Si no hay nada que restaurar, ya estamos "listos".
                var restored by remember(current) { mutableStateOf(current.restore == null) }

                Box {
                    NavHost(
                        navController = navController,
                        startDestination = current.root,
                        enterTransition = ArraxTransitions.enter,
                        exitTransition = ArraxTransitions.exit,
                        popEnterTransition = ArraxTransitions.popEnter,
                        popExitTransition = ArraxTransitions.popExit
                    ) {
                        authGraph(navController, startOnVerify = current.needsVerification)
                        lobbyGraph(navController)
                        businessGraph(navController) // ya incluye eventGraph adentro
                    }

                    // Tapa mientras armamos la pila para no ver el lobby de pasada
                    if (!restored) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                        ) { ArxLoading() }
                    }
                }

                // Pila sintética: Lobby (ya es la raíz) -> Business -> Event
                LaunchedEffect(current) {
                    when (val target = current.restore) {
                        is StartTarget.Business ->
                            navController.navigate(BusinessHome(target.businessId))

                        is StartTarget.Event -> {
                            navController.navigate(BusinessHome(target.businessId))
                            navController.navigate(EventShell(target.businessId, target.eventId))
                        }

                        else -> Unit
                    }
                    restored = true
                }

                // Solo empezamos a guardar cuando la restauración terminó
                if (restored) {
                    TrackLastContext(navController, viewModel)
                }
            }
        }
    }
}

/** Guarda solo IDs cada vez que el usuario llega a un "punto de reanudación". */
@Composable
private fun TrackLastContext(
    navController: NavHostController,
    viewModel: AppStartViewModel
) {
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { entry ->
            val dest = entry.destination
            when {
                dest.hasRoute<EventShell>() -> {
                    val args = entry.toRoute<EventShell>()
                    viewModel.saveContext(args.businessId, args.eventId)
                }
                dest.hasRoute<BusinessHome>() -> {
                    viewModel.saveContext(entry.toRoute<BusinessHome>().businessId, null)
                }
                dest.hasRoute<BusinessEvents>() -> {
                    viewModel.saveContext(entry.toRoute<BusinessEvents>().businessId, null)
                }
                // Lobby o Login: el usuario "soltó" el contexto (o cerró sesión)
                dest.hasRoute<LobbyHome>() || dest.hasRoute<Login>() -> viewModel.clearContext()
            }
        }
    }
}