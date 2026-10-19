package com.example.arrax.ui.root.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.example.arrax.core.navigation.AuthGraph

private fun NavController.isCurrentEntryResumed(): Boolean =
    currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

/** Navega solo si la pantalla actual está activa (ignora doble toque durante la transición). */
fun <T : Any> NavController.safeNavigate(
    route: T,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    if (isCurrentEntryResumed()) navigate(route, builder)
}

fun NavController.safePopBackStack() {
    if (isCurrentEntryResumed()) popBackStack()
}

/** Lambda lista para pasar como `onBack`. */
fun NavController.backAction(): () -> Unit = { safePopBackStack() }

/** Logout / sesión inválida: limpia todo el back stack y vuelve a Auth. */
fun NavController.logoutToAuth() {
    navigate(AuthGraph) { popUpTo(graph.id) { inclusive = true } }
}