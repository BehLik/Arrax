package com.example.arrax.ui.screen.events

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.arrax.core.navigation.*
import com.example.arrax.ui.root.navigation.PlaceholderAction
import com.example.arrax.ui.root.navigation.PlaceholderScreen
import com.example.arrax.ui.root.navigation.backAction
import com.example.arrax.ui.root.navigation.safeNavigate
import com.example.arrax.ui.screen.events.shell.EventShellScreen

fun NavGraphBuilder.eventGraph(nav: NavController) {
    eventShell(nav)
    eventOrders(nav)
    eventWeighing(nav)
    eventSettlement(nav)
    eventFinance(nav)
}

// ── Contenedor con bottom bar (Inicio, Pedidos, Entregas, Más) ──
private fun NavGraphBuilder.eventShell(nav: NavController) {
    composable<EventShell> { entry ->
        EventShellScreen(args = entry.toRoute<EventShell>(), outerNav = nav)
    }
}

// ── Pedidos: crear/editar y detalle (sin bottom bar) ──
private fun NavGraphBuilder.eventOrders(nav: NavController) {
    val back = nav.backAction()

    composable<EventOrderEdit> { entry ->
        val args = entry.toRoute<EventOrderEdit>()
        PlaceholderScreen(
            title = if (args.orderId == null) "Nuevo pedido" else "Editar pedido",
            onBack = back
        )
    }

    composable<EventOrderDetail> { entry ->
        val args = entry.toRoute<EventOrderDetail>()
        PlaceholderScreen(
            title = "Detalle del pedido",
            onBack = back,
            actions = listOf(
                PlaceholderAction("Editar") {
                    nav.safeNavigate(EventOrderEdit(args.businessId, args.eventId, args.orderId))
                }
            )
        )
    }
}

// ── Pesaje ── (sin cambios)
private fun NavGraphBuilder.eventWeighing(nav: NavController) {
/* igual que ahora */
}

private fun NavGraphBuilder.eventSettlement(nav: NavController) {
    val back = nav.backAction()
    composable<EventSettlement> { PlaceholderScreen("Entregas y cobros", back) }
    composable<EventSettlementDetail> { PlaceholderScreen("Cobro y entrega del cliente", back) }
}

// ── Gastos y resultados ── (sin cambios)
private fun NavGraphBuilder.eventFinance(nav: NavController) {
/* igual que ahora */
}