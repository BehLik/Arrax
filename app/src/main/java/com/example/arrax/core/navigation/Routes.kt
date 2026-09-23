package com.example.arrax.core.navigation

import kotlinx.serialization.Serializable

sealed class Routes(val route: String) {
    // Auth
    data object Login : Routes("auth/login")
    data object Register : Routes("auth/register")
    data object ForgotPassword : Routes("auth/forgot")
    // Lobby
    data object LobbyOnboarding : Routes("lobby/onboarding")
    data object LobbyHome : Routes("lobby/home")
    // Evento
    data object EventCreate : Routes("evento/create")
    data object EventDetail : Routes("evento/detail/{eventoId}")
}
////Grafos raiz
//@Serializable
//object OnboardingGraph
//
//@Serializable
//object AuthGraph
//
//@Serializable
//object LobbyGraph
//
//@Serializable
//object EventGraph
//
////Pantallas de Onboarding-----------------------------------
//@Serializable
//object OnboardingWelcome
//
////Pantallas de Authentication-------------------------------
//@Serializable
//object AuthLogin
//
//@Serializable
//object AuthRegister
//
//@Serializable
//object AuthVerification
//
//@Serializable
//object AuthForgotPassword
////Pantallas de Lobby----------------------------------------
//
//@Serializable
//object LobbyScaffoldRoute
//
//@Serializable
//object LobbyHome
//
//@Serializable
//object LobbyProfile
//
//@Serializable
//object LobbyEditProfile
//
//@Serializable
//object LobbySetting
//
////Pantallas de Event----------------------------------------
//
//@Serializable
//object EventScaffoldRoute
//
//@Serializable
//object EventDashboard
//
//@Serializable
//object EventProfile
//
//@Serializable
//object EventEditProfile
//
//@Serializable
//object EventSetting
//
//@Serializable
//object Event