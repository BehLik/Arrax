package com.example.arrax.ui.screen.events

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.arrax.core.navigation.*
import com.example.arrax.ui.root.navigation.PlaceholderScreen
import com.example.arrax.ui.root.navigation.backAction
import com.example.arrax.ui.root.navigation.safeNavigate
import com.example.arrax.ui.screen.events.home.subscreen.edit.EventEditScreen
import com.example.arrax.ui.screen.events.home.subscreen.viewdetail.EventViewDetailScreen
import com.example.arrax.ui.screen.events.orders.subscreen.make.EventMakeOrderScreen
import com.example.arrax.ui.screen.events.orders.subscreen.view.EventViewOrderScreen
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
    composable<EventDetail> { entry ->
        val args = entry.toRoute<EventDetail>()
        EventViewDetailScreen(
            onBack = { nav.popBackStack() },
            onEditClick = { nav.safeNavigate(EventEdit(args.businessId, args.eventId)) }
        )
    }
    composable<EventEdit> {
        EventEditScreen(
            onBack = { nav.popBackStack() },
            onSaved = { nav.popBackStack() }
        )
    }
    composable<EventMakeOrder> { entry ->
        val args = entry.toRoute<EventMakeOrder>()
        val selectedId by entry.savedStateHandle
            .getStateFlow<String?>(SELECTED_CUSTOMER_ID_KEY, null)
            .collectAsStateWithLifecycle()

        EventMakeOrderScreen(
            onBack = { nav.popBackStack() },
            onSelectCustomer = { nav.safeNavigate(BusinessCustomers(args.businessId, selectMode = true)) },
            selectedCustomerId = selectedId,
            onSelectedCustomerConsumed = { entry.savedStateHandle[SELECTED_CUSTOMER_ID_KEY] = null },
        )
    }
}

// ── Pedidos: crear/editar y detalle (sin bottom bar) ──
private fun NavGraphBuilder.eventOrders(nav: NavController) {
    val back = nav.backAction()

//    composable<EventPlaceOrder> { entry ->
//        val args = entry.toRoute<EventPlaceOrder>()
//        EventPlaceOrderScreen(
////            title = if (args.orderId == null) "Nuevo pedido" else "Editar pedido",
////            onBack = back
//        )
//    }

    composable<EventOrderView> { entry ->
        val args = entry.toRoute<EventOrderView>()
        EventViewOrderScreen(
//            title = "Detalle del pedido",
//            onBack = back,
//            actions = listOf(
//                PlaceholderAction("Editar") {
//                    nav.safeNavigate(EventOrderView(args.businessId, args.eventId, args.orderId))
//                }
//            )
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