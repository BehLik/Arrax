package com.example.arrax.ui.screen.lobby

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.arrax.ui.screen.lobby.join.LobbyJoinScreen
import com.example.arrax.ui.screen.lobby.notification.LobbyNotificationScreen
import com.example.arrax.ui.screen.lobby.profile.LobbyProfileScreen
import com.example.arrax.ui.screen.lobby.settings.LobbySettingsScreen
import com.example.arrax.ui.screen.lobby.settings.component.AlertChannel

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
            // TODO: mover a LobbyJoinViewModel; al unirse: nav.safeNavigate(BusinessHome(id)) { popUpTo<BusinessJoin> { inclusive = true } }
            var method by rememberSaveable { mutableStateOf(JoinMethod.CODE) }
            var code by rememberSaveable { mutableStateOf("") }
            var torchOn by remember { mutableStateOf(false) }

            LobbyJoinScreen(
                method = method, code = code, isLoading = false, errorMessage = null, torchOn = torchOn,
                onBack = back,
                onMethodChange = { method = it },
                onCodeChange = { code = it },
                onJoin = { /* JoinTenantUseCase vía ViewModel */ },
                onTorchToggle = { torchOn = !torchOn }
            )
        }

        composable<Profile> {
            // TODO: estado desde LobbyProfileViewModel
            LobbyProfileScreen(
                userName = "Admin", userEmail = "admin@arrax.com", activeBusinessName = null,
                onBack = back,
                onOptionClick = { /* TODO */ },
                onLogout = { nav.logoutToAuth() }
            )
        }
        composable<Settings> {
            LobbySettingsScreen(
                alertStates = AlertChannel.entries.associateWith { true }, appVersion = "v1.0.0",
                onBack = back, onPreferenceClick = {}, onAlertToggle = { _, _ -> }, onAboutLinkClick = {}
            )
        }
        composable<Notifications> {
            LobbyNotificationScreen(notifications = emptyList(), onBack = back, onNotificationClick = {})
        }
    }
}