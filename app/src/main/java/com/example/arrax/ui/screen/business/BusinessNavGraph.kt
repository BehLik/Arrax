package com.example.arrax.ui.screen.business

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.arrax.core.designsystem.access.ProvideAccess
import com.example.arrax.core.navigation.*
import com.example.arrax.domain.model.Permission
import com.example.arrax.ui.root.navigation.PlaceholderScreen
import com.example.arrax.ui.root.navigation.backAction
import com.example.arrax.ui.root.navigation.safeNavigate
import com.example.arrax.ui.screen.business.alerts.BusinessAlertsScreen
import com.example.arrax.ui.screen.business.customers.BusinessCustomersScreen
import com.example.arrax.ui.screen.business.customers.subscreen.add.CustomerAddScreen
import com.example.arrax.ui.screen.business.customers.subscreen.view.CustomerViewScreen
import com.example.arrax.ui.screen.business.event.BusinessEventCreateScreen
import com.example.arrax.ui.screen.business.event.subscreen.view.BusinessEventViewScreen
import com.example.arrax.ui.screen.business.home.BusinessHomeScreen
import com.example.arrax.ui.screen.business.more.BusinessMoreScreen
import com.example.arrax.ui.screen.business.more.subscreen.catalog.BusinessCatalogScreen
import com.example.arrax.ui.screen.business.more.subscreen.members.BusinessMembersScreen
import com.example.arrax.ui.screen.business.more.subscreen.members.subscreen.invitation.MembersInvitationScreen
import com.example.arrax.ui.screen.business.more.subscreen.members.subscreen.invitationdetail.MembersInvitationDetailScreen
import com.example.arrax.ui.screen.events.eventGraph

/*
 * En core/navigation (junto a tus otras rutas) agrega:
 *
 *     @Serializable object BusinessGraph
 *
 * Nunca navegas al grafo directo: siempre entras por un hijo
 * (BusinessHome, EventShell...). Por eso el startDestination solo
 * es "de adorno". Requiere navigation-compose 2.8+.
 */

// ─────────────────────────────────────────────
// Grafo: TODO lo de un negocio vive aquí adentro
// ─────────────────────────────────────────────
fun NavGraphBuilder.businessGraph(nav: NavController) {
    navigation<BusinessGraph>(startDestination = BusinessHome::class) {
        businessCore(nav)
        businessAlerts(nav)
        businessMembers(nav)
        businessCustomers(nav)
        businessMore(nav)
        eventGraph(nav) // el evento ahora cuelga del negocio
    }
}

// ─────────────────────────────────────────────
// Guardia: se usa en TODAS las rutas del negocio y del evento
// ─────────────────────────────────────────────
internal inline fun <reified T : Any> NavGraphBuilder.businessComposable(
    nav: NavController,
    required: Permission? = null,
    noinline content: @Composable (NavBackStackEntry) -> Unit
) {
    composable<T> { entry ->
        ProvideAccess(
            onRevoked = { nav.exitBusiness() },
            required = required
        ) { content(entry) }
    }
}

// ─────────────────────────────────────────────
// Entrar / salir de un negocio (arregla el bucle lobby <-> business)
// ─────────────────────────────────────────────
fun NavController.openBusiness(businessId: String) {
    navigate(BusinessHome(businessId)) {
        // si ya había otro negocio abierto, lo cerramos completo
        popUpTo<BusinessGraph> { inclusive = true }
        launchSingleTop = true
    }
}

fun NavController.exitBusiness() {
    // Vuelve al lobby que ya está debajo. Si no hay pila
    // (arranque directo), lo crea desde cero.
    if (!popBackStack<BusinessGraph>(inclusive = true)) {
        navigate(LobbyGraph) { popUpTo(0) }
    }
}

// ── Inicio y eventos del negocio ──
private fun NavGraphBuilder.businessCore(nav: NavController) {
    val back = nav.backAction()

    businessComposable<BusinessHome>(nav) { entry ->
        val args = entry.toRoute<BusinessHome>()
        BusinessHomeScreen(
            onBack = back,
            onCreateEventClick = { nav.safeNavigate(EventCreate(args.businessId)) },
            onCreateCustomerClick = { nav.safeNavigate(BusinessCustomerAdd(args.businessId)) },
            onTeamClick = { nav.safeNavigate(BusinessMembers(args.businessId)) },
            // TODO: abrir directo el evento activo -> EventShell(args.businessId, eventId)
            onEventClick = { nav.safeNavigate(BusinessEvents(args.businessId)) },
            onEventsClick = { nav.safeNavigate(BusinessEvents(args.businessId)) },
            onBusinessClick = { nav.exitBusiness() }, // antes: navigate(LobbyGraph)
            onMoreClick = { nav.safeNavigate(BusinessMore(args.businessId)) },
            onAlertsClick = { nav.safeNavigate(BusinessAlerts(args.businessId)) },
            onCustomersClick = { nav.safeNavigate(BusinessCustomers(args.businessId)) },
        )
    }

    businessComposable<BusinessEvents>(nav, Permission.EVENT_VIEW) { entry ->
        val args = entry.toRoute<BusinessEvents>()
        BusinessEventViewScreen(
            onBack = back,
            onEventClick = { eventId -> nav.safeNavigate(EventShell(args.businessId, eventId)) }
        )
    }

    businessComposable<EventCreate>(nav, Permission.EVENT_CREATE) { entry ->
        val args = entry.toRoute<EventCreate>()
        BusinessEventCreateScreen(
            onBack = back,
            onEventCreated = {
                // TODO: que devuelva eventId y abrir EventShell(args.businessId, eventId)
                nav.navigate(BusinessEvents(args.businessId)) {
                    popUpTo<EventCreate> { inclusive = true }
                }
            },
            onBackBusiness = {},
        )
    }
}

// ── Alertas ──
private fun NavGraphBuilder.businessAlerts(nav: NavController) {
    val back = nav.backAction()

    businessComposable<BusinessAlerts>(nav) {
        BusinessAlertsScreen(
            onBack = back,
            // TODO: navegar según la categoría (evento, invitación, pedido).
            onAlertClick = { _ -> }
        )
    }
}

// ── Miembros ──
private fun NavGraphBuilder.businessMembers(nav: NavController) {
    val back = nav.backAction()

    businessComposable<BusinessMembers>(nav) { entry ->
        val args = entry.toRoute<BusinessMembers>()
        BusinessMembersScreen(
            onBack = back,
            onCreateInvitationClick = { nav.safeNavigate(InvitationCreate(args.businessId)) },
            onInvitationClick = { code -> nav.safeNavigate(InvitationDetail(args.businessId, code)) }
        )
    }

    businessComposable<BusinessMemberDetail>(nav, Permission.MEMBERS_MANAGE) {
        PlaceholderScreen("Detalle del miembro", back)
    }

    businessComposable<InvitationCreate>(nav, Permission.INVITE_CREATE) {
        MembersInvitationScreen(
            onBack = back,
            onDone = { nav.popBackStack<BusinessMembers>(inclusive = false) }
        )
    }

    businessComposable<InvitationDetail>(nav, Permission.INVITE_CREATE) {
        MembersInvitationDetailScreen(onBack = back)
    }
}

// ── Clientes ──
private fun NavGraphBuilder.businessCustomers(nav: NavController) {
    val back = nav.backAction()

    businessComposable<BusinessCustomers>(nav) { entry ->
        val args = entry.toRoute<BusinessCustomers>()
        BusinessCustomersScreen(
            onBack = back,
            onAddCustomerClick = { nav.safeNavigate(BusinessCustomerAdd(args.businessId)) },
            onCustomerClick = { customerId ->
                if (args.selectMode) {
                    nav.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(SELECTED_CUSTOMER_ID_KEY, customerId)
                    nav.popBackStack()
                } else {
                    nav.safeNavigate(BusinessCustomerView(args.businessId, customerId))
                }
            }
        )
    }

    businessComposable<BusinessCustomerAdd>(nav) {
        CustomerAddScreen(onBack = back, onDone = back)
    }

    businessComposable<BusinessCustomerView>(nav) { entry ->
        val args = entry.toRoute<BusinessCustomerView>()
        CustomerViewScreen(
            onBack = back,
            onEditClick = { id -> nav.safeNavigate(BusinessCustomerAdd(args.businessId, id)) },
            onDeleted = back,
        )
    }
}

// ── Más ──
private fun NavGraphBuilder.businessMore(nav: NavController) {
    val back = nav.backAction()

    businessComposable<BusinessMore>(nav) { entry ->
        val args = entry.toRoute<BusinessMore>()
        BusinessMoreScreen(
            onTeamClick = { nav.safeNavigate(BusinessMembers(args.businessId)) },
            onCatalogClick = { nav.safeNavigate(BusinessCatalog(args.businessId)) },
            onHistoryClick = { nav.safeNavigate(BusinessHistory(args.businessId)) },
            onSettingsClick = { nav.safeNavigate(BusinessSettings(args.businessId)) },
            onBack = back,
            // TODO: Ayuda y Acerca de (rutas aún no definidas).
            onHelpClick = {},
        )
    }

    businessComposable<BusinessSettings>(nav) { PlaceholderScreen("Ajustes del negocio", back) }
    businessComposable<BusinessCatalog>(nav) { BusinessCatalogScreen(onBackClick = back) }
    businessComposable<BusinessHistory>(nav) { PlaceholderScreen("Historial del negocio", back) }
}
