package com.example.arrax.ui.screen.lobby.onboarding

data class LobbyOnboardingState(
    val modo: OnboardingModo = OnboardingModo.SELECCION,
    val isResolviendoTenant: Boolean = true, // chequeo inicial contra Firestore antes de mostrar UI
    val nombreNegocio: String = "",
    val nombreNegocioError: String? = null,
    val codigoInvitacion: String = "",
    val codigoInvitacionError: String? = null,
    val isLoading: Boolean = false,
    val errorGeneral: String? = null
)

enum class OnboardingModo { SELECCION, CREAR_NEGOCIO, UNIRSE_NEGOCIO }