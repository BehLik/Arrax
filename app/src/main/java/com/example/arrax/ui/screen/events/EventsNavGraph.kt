package com.example.arrax.ui.screen.events

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.arrax.core.navigation.*
import com.example.arrax.domain.model.Permission
import com.example.arrax.ui.root.navigation.PlaceholderScreen
import com.example.arrax.ui.root.navigation.backAction
import com.example.arrax.ui.root.navigation.safeNavigate
import com.example.arrax.ui.screen.business.businessComposable
import com.example.arrax.ui.screen.events.home.subscreen.edit.EventEditScreen
import com.example.arrax.ui.screen.events.home.subscreen.viewdetail.EventViewDetailScreen
import com.example.arrax.ui.screen.events.orders.subscreen.make.EventMakeOrderScreen
import com.example.arrax.ui.screen.events.orders.subscreen.view.EventViewOrderScreen
import com.example.arrax.ui.screen.events.shell.EventShellScreen


// ─────────────────────────────────────────────
// Grafo: todo lo de UN evento vive aquí adentro
// ─────────────────────────────────────────────
fun NavGraphBuilder.eventGraph(nav: NavController) {
    navigation<EventGraph>(startDestination = EventShell::class) {
        eventShell(nav)
        eventOrders(nav)
        eventWeighing(nav)
        eventSettlement(nav)
        eventFinance(nav)
    }
}

// ─────────────────────────────────────────────
// Entrar / salir de un evento
// ─────────────────────────────────────────────
fun NavController.openEvent(businessId: String, eventId: String) {
    navigate(EventShell(businessId, eventId)) {
        // si había otro evento abierto, lo cerramos completo
        popUpTo<EventGraph> { inclusive = true }
        launchSingleTop = true
    }
}

fun NavController.exitEvent(businessId: String) {
    // Vuelve a lo que esté debajo (lista de eventos o inicio del negocio).
    // Si no hay pila (arranque directo), cae a la lista de eventos.
    if (!popBackStack<EventGraph>(inclusive = true)) {
        navigate(BusinessEvents(businessId))
    }
}

// ── Contenedor con bottom bar (Inicio, Pedidos, Entregas, Más) ──
private fun NavGraphBuilder.eventShell(nav: NavController) {
    val back = nav.backAction()

    businessComposable<EventShell>(nav, Permission.EVENT_VIEW) { entry ->
        EventShellScreen(args = entry.toRoute<EventShell>(), outerNav = nav)
    }

    businessComposable<EventDetail>(nav, Permission.EVENT_VIEW) { entry ->
        val args = entry.toRoute<EventDetail>()
        EventViewDetailScreen(
            onBack = back,
            onEditClick = { nav.safeNavigate(EventEdit(args.businessId, args.eventId)) }
        )
    }

    // TODO: si existe un permiso EVENT_EDIT, pásalo aquí como required
    businessComposable<EventEdit>(nav) {
        EventEditScreen(
            onBack = back,
            onSaved = back
        )
    }
}

// ── Pedidos: crear y detalle (sin bottom bar) ──
private fun NavGraphBuilder.eventOrders(nav: NavController) {
    val back = nav.backAction()

    businessComposable<EventMakeOrder>(nav) { entry ->
        val args = entry.toRoute<EventMakeOrder>()
        val selectedId by entry.savedStateHandle
            .getStateFlow<String?>(SELECTED_CUSTOMER_ID_KEY, null)
            .collectAsStateWithLifecycle()

        EventMakeOrderScreen(
            onBack = back,
            onSelectCustomer = {
                nav.safeNavigate(BusinessCustomers(args.businessId, selectMode = true))
            },
            selectedCustomerId = selectedId,
            onSelectedCustomerConsumed = {
                entry.savedStateHandle[SELECTED_CUSTOMER_ID_KEY] = null
            },
        )
    }

    businessComposable<EventOrderView>(nav) {
        // TODO: pasar onBack = back cuando la pantalla lo reciba
        EventViewOrderScreen()
    }
}

// ── Pesaje ──
private fun NavGraphBuilder.eventWeighing(nav: NavController) {
    // composable<X> { ... }  por  businessComposable<X>(nav) { ... }
}

// ── Entregas y cobros ──
private fun NavGraphBuilder.eventSettlement(nav: NavController) {
    val back = nav.backAction()

    businessComposable<EventSettlement>(nav) {
        PlaceholderScreen("Entregas y cobros", back)
    }
    businessComposable<EventSettlementDetail>(nav) {
        PlaceholderScreen("Cobro y entrega del cliente", back)
    }
}

// ── Gastos y resultados ──
private fun NavGraphBuilder.eventFinance(nav: NavController) {

}
