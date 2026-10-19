package com.example.arrax.ui.screen.lobby

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.arrax.core.navigation.*
import com.example.arrax.ui.root.navigation.PlaceholderAction
import com.example.arrax.ui.root.navigation.PlaceholderScreen
import com.example.arrax.ui.root.navigation.backAction
import com.example.arrax.ui.root.navigation.logoutToAuth
import com.example.arrax.ui.root.navigation.safeNavigate
import com.example.arrax.ui.screen.lobby.home.LobbyHomeScreen

fun NavGraphBuilder.lobbyGraph(nav: NavController) {
    val back = nav.backAction()

    navigation<LobbyGraph>(startDestination = LobbyHome) {

        composable<LobbyHome>(
            enterTransition = { fadeIn(tween(400)) },
            popExitTransition = { fadeOut(tween(400)) }
        ) {
            LobbyHomeScreen(
                onBusinessClick = { id -> nav.safeNavigate(BusinessHome(id)) },
                onCreateBusinessClick = { nav.safeNavigate(BusinessCreate) },
                onJoinClick = { nav.safeNavigate(BusinessJoin) },
                onViewAllClick = { nav.safeNavigate(MyBusinesses) },
                onNotificationsClick = { nav.safeNavigate(Notifications) },
                onSettingsClick = { nav.safeNavigate(Settings) },
                onProfileClick = { nav.safeNavigate(Profile) }
            )
        }

        composable<MyBusinesses> {
            // TODO: onBusinessClick = { id -> nav.safeNavigate(BusinessHome(id)) }
            PlaceholderScreen("Mis negocios", back)
        }

        composable<BusinessCreate> {
            // TODO: BusinessCreateScreen(onBack = back, onCreated = { id ->
            //     nav.safeNavigate(BusinessHome(id)) { popUpTo<BusinessCreate> { inclusive = true } } })
            PlaceholderScreen("Crear negocio", back)
        }

        composable<BusinessJoin> {
            // TODO: BusinessJoinScreen(onBack = back, onJoined = { id ->
            //     nav.safeNavigate(BusinessHome(id)) { popUpTo<BusinessJoin> { inclusive = true } } })
            PlaceholderScreen("Unirse a un negocio", back)
        }

        composable<Profile> { PlaceholderScreen("Perfil", back) }

        composable<Settings> {
            // TODO: onLogout = { nav.logoutToAuth() }
            PlaceholderScreen(
                title = "Ajustes",
                onBack = back,
                actions = listOf(PlaceholderAction("Cerrar sesión") { nav.logoutToAuth() })
            )
        }

        composable<Notifications> { PlaceholderScreen("Notificaciones", back) }
    }
}