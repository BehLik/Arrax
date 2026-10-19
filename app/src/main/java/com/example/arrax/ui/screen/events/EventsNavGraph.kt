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
import com.example.arrax.ui.screen.events.create.EventCreateScreen
import com.example.arrax.ui.screen.events.detail.EventDetailScreen

fun NavGraphBuilder.eventGraph(nav: NavController) {
    eventCore(nav)
    eventOrders(nav)
    eventWeighing(nav)
    eventSettlement(nav)
    eventFinance(nav)
}

// ── Crear evento y pantalla principal del evento ──
private fun NavGraphBuilder.eventCore(nav: NavController) {
    val back = nav.backAction()

    composable<EventCreate> { entry ->
        val args = entry.toRoute<EventCreate>()
        EventCreateScreen(
            onBack = back,
            onEventCreated = { eventId ->
                nav.safeNavigate(EventHome(args.businessId, eventId)) {
                    popUpTo<EventCreate> { inclusive = true }
                }
            }
        )
    }

    composable<EventHome> {
        EventDetailScreen(onBack = back)
        // TODO: cuando EventDetailScreen exponga estos callbacks, conéctalos con:
        //   val args = entry.toRoute<EventHome>()
        //   onOrdersClick     = { nav.safeNavigate(EventOrders(args.businessId, args.eventId)) }
        //   onWeighingClick   = { nav.safeNavigate(EventWeighingSetup(args.businessId, args.eventId)) }
        //   onSettlementClick = { nav.safeNavigate(EventSettlement(args.businessId, args.eventId)) }
        //   onExpensesClick   = { nav.safeNavigate(EventExpenses(args.businessId, args.eventId)) }
        //   onResultsClick    = { nav.safeNavigate(EventResults(args.businessId, args.eventId)) }
    }
}

// ── Pedidos: lista, crear/editar y detalle ──
private fun NavGraphBuilder.eventOrders(nav: NavController) {
    val back = nav.backAction()

    composable<EventOrders> { entry ->
        val args = entry.toRoute<EventOrders>()
        // TODO: onOrderClick = { id -> nav.safeNavigate(EventOrderDetail(args.businessId, args.eventId, id)) }
        PlaceholderScreen(
            title = "Pedidos",
            onBack = back,
            actions = listOf(
                PlaceholderAction("Nuevo pedido") {
                    nav.safeNavigate(EventOrderEdit(args.businessId, args.eventId))
                }
            )
        )
    }

    composable<EventOrderEdit> { entry ->
        val args = entry.toRoute<EventOrderEdit>()
        // TODO: al guardar -> nav.safePopBackStack(); selector de cliente como bottom sheet
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

// ── Pesaje: elegir modo y pesar (incluye manos libres) ──
private fun NavGraphBuilder.eventWeighing(nav: NavController) {
    val back = nav.backAction()

    composable<EventWeighingSetup> { entry ->
        val args = entry.toRoute<EventWeighingSetup>()
        PlaceholderScreen(
            title = "Modo de pesaje",
            onBack = back,
            actions = listOf(
                PlaceholderAction("Por corte") {
                    nav.safeNavigate(EventWeighing(args.businessId, args.eventId, WeighingMode.BY_CUT))
                },
                PlaceholderAction("Por cliente") {
                    nav.safeNavigate(EventWeighing(args.businessId, args.eventId, WeighingMode.BY_CUSTOMER))
                },
                PlaceholderAction("Por corte · manos libres") {
                    nav.safeNavigate(
                        EventWeighing(args.businessId, args.eventId, WeighingMode.BY_CUT, handsFree = true)
                    )
                }
            )
        )
    }

    composable<EventWeighing> { entry ->
        val args = entry.toRoute<EventWeighing>()
        // TODO: pesaje real; args.mode y args.handsFree definen la cola y el modo de voz
        PlaceholderScreen(
            title = "Pesaje · ${args.mode}" + if (args.handsFree) " · manos libres" else "",
            onBack = back
        )
    }
}

// ── Entregas y cobros ──
private fun NavGraphBuilder.eventSettlement(nav: NavController) {
    val back = nav.backAction()

    composable<EventSettlement> {
        // TODO: onCustomerClick = { id -> nav.safeNavigate(EventSettlementDetail(businessId, eventId, id)) }
        PlaceholderScreen("Entregas y cobros", back)
    }

    composable<EventSettlementDetail> { PlaceholderScreen("Cobro y entrega del cliente", back) }
}

// ── Gastos y resultados ──
private fun NavGraphBuilder.eventFinance(nav: NavController) {
    val back = nav.backAction()

    composable<EventExpenses> { PlaceholderScreen("Gastos del evento", back) }
    composable<EventResults> { PlaceholderScreen("Resultados del evento", back) }
}