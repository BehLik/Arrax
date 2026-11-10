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
import com.example.arrax.ui.screen.events.home.EventHomeScreen
import com.example.arrax.ui.screen.events.more.EventMoreScreen
import com.example.arrax.ui.screen.events.orders.EventOrderScreen

fun NavGraphBuilder.eventGraph(nav: NavController) {
    eventCore(nav)
    eventOrders(nav)
    eventWeighing(nav)
    eventSettlement(nav)
    eventFinance(nav)
    eventMore(nav)
}

// ── Crear evento y pantalla principal del evento ──
private fun NavGraphBuilder.eventCore(nav: NavController) {
    val back = nav.backAction()

    composable<EventHome> { entry ->
        val args = entry.toRoute<EventHome>()
        EventHomeScreen(
            onBack = back,
            onEditEventClick = {},
            onEditEventNameClick = {},
            onSeeDetailClick = {},
            onMakeDeliveriesClick = {},
            onBottomBarNavigate = {},
            onMakeOrdersClick = { nav.safeNavigate(EventOrders(args.businessId, args.eventId)) },
            onSeeAllOrdersClick = { nav.safeNavigate(EventOrders(args.businessId, args.eventId)) },
            onOrderClick = { id -> nav.safeNavigate(EventOrderDetail(args.businessId, args.eventId, id)) },
            onHandsFreeWeighingClick = {
                nav.safeNavigate(
                    EventWeighing(args.businessId, args.eventId, WeighingMode.BY_CUT, handsFree = true)
                )
            }
        )
    }
}

// ── Pedidos: lista, crear/editar y detalle ──
private fun NavGraphBuilder.eventOrders(nav: NavController) {
    val back = nav.backAction()

    composable<EventOrders> { entry ->
        val args = entry.toRoute<EventOrders>()
        EventOrderScreen()
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
private fun NavGraphBuilder.eventMore(nav: NavController){
    val back=nav.backAction()
    composable<EventMore>{
        EventMoreScreen()
    }
}