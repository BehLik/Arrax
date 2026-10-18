package com.example.arrax.ui.screen.lobby

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.arrax.core.navigation.BusinessCreate
import com.example.arrax.core.navigation.BusinessHome
import com.example.arrax.core.navigation.BusinessJoin
import com.example.arrax.core.navigation.LobbyHome
import com.example.arrax.core.navigation.Notifications
import com.example.arrax.core.navigation.Profile
import com.example.arrax.core.navigation.Settings
import com.example.arrax.ui.screen.lobby.home.LobbyHomeScreen

fun NavGraphBuilder.lobbyGraph(nav: NavController) {
    composable<LobbyHome> {
        LobbyHomeScreen(
            onBusinessClick = { id -> nav.navigate(BusinessHome(id)) },
            onCreateBusinessClick = { nav.navigate(BusinessCreate) },
            onJoinClick = { nav.navigate(BusinessJoin) },
            onViewAllClick = { },
            onNotificationsClick = { nav.navigate(Notifications) },
            onSettingsClick = { nav.navigate(Settings) },
            onProfileClick = { nav.navigate(Profile) }
        )
    }
    composable<BusinessCreate> {
        // BusinessCreateScreen(...)
    }
}