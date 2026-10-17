package com.example.arrax.ui.root.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.arrax.core.navigation.BusinessCreate
import com.example.arrax.core.navigation.BusinessHome
import com.example.arrax.core.navigation.BusinessJoin
import com.example.arrax.core.navigation.LobbyGraph
import com.example.arrax.core.navigation.LobbyHome
import com.example.arrax.core.navigation.Profile
import com.example.arrax.ui.screen.lobby.home.LobbyHomeScreen

// LobbyNavGraph.kt
fun NavGraphBuilder.lobbyGraph(nav: NavController) {
    navigation<LobbyGraph>(startDestination = LobbyHome) {
        composable<LobbyHome> {
            LobbyHomeScreen(
                onBusinessClick = { id -> nav.navigate(BusinessHome(id)) },
                onCreateBusinessClick = { nav.navigate(BusinessCreate) },
                onJoinBusinessClick = { nav.navigate(BusinessJoin) },
                onProfileClick = { nav.navigate(Profile) }
            )
        }
        composable<BusinessCreate> {
//            BusinessCreateScreen(
//                onBack = nav::popBackStack,
//                onCreated = { id ->
//                    nav.navigate(BusinessHome(id)) {
//                        popUpTo<BusinessCreate> { inclusive = true }
//                    }
//                }
//            )
        }
        // BusinessJoin, Profile, Settings, Notifications...
    }
}