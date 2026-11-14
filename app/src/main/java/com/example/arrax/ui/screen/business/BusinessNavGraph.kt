package com.example.arrax.ui.screen.business

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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

fun NavGraphBuilder.businessGraph(nav: NavController) {
    businessCore(nav)
    businessAlerts(nav)
    businessMembers(nav)
    businessCustomers(nav)
    businessMore(nav)
}

private inline fun <reified T : Any> NavGraphBuilder.businessComposable(
    nav: NavController,
    required: Permission? = null,
    noinline content: @Composable (NavBackStackEntry) -> Unit
) {
    composable<T> { entry ->
        ProvideAccess(
            onRevoked = {
                nav.navigate(LobbyHome) { popUpTo(0) { inclusive = true } }
            },
            required = required
        ) { content(entry) }
    }
}
// ── Inicio y eventos del negocio ──
private fun NavGraphBuilder.businessCore(nav: NavController) {
    val back = nav.backAction()

    businessComposable<BusinessHome>(nav){ entry ->
        val args = entry.toRoute<BusinessHome>()
        BusinessHomeScreen(
            onBack = back,
            onCreateEventClick = { nav.safeNavigate(EventCreate(args.businessId)) },
            onCreateCustomerClick = { nav.safeNavigate(BusinessCustomerAdd(args.businessId)) },
            onTeamClick = {nav.safeNavigate(BusinessMembers(args.businessId))},
            // TODO: cuando exista EventHome -> nav.safeNavigate(EventHome(args.businessId, eventId))
            onEventClick = { nav.safeNavigate(BusinessEvents(args.businessId)) },
            onEventsClick = { nav.safeNavigate(BusinessEvents(args.businessId)) },
            onBusinessClick = { nav.safeNavigate(LobbyGraph) },
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
                nav.navigate(BusinessEvents(args.businessId)) {
                    popUpTo<EventCreate> { inclusive = true }
                }
            },
            onBackBusiness = {},

        )
    }
}

// ── Alertas y recordatorios ──
private fun NavGraphBuilder.businessAlerts(nav: NavController) {
    val back = nav.backAction()

    composable<BusinessAlerts> {
        BusinessAlertsScreen(
            onBack = back,
            // TODO: navegar según la categoría de la alerta (evento, invitación, pedido).
            onAlertClick = { _ -> }
        )
    }
}

// ── Miembros: lista + invitación (bottom sheet) y detalle ──
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

    businessComposable<BusinessMemberDetail>(nav, Permission.MEMBERS_MANAGE) { PlaceholderScreen("Detalle del miembro", back) }

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
// ── Clientes: lista, crear/editar y detalle ──
private fun NavGraphBuilder.businessCustomers(nav: NavController) {
    val back = nav.backAction()

    composable<BusinessCustomers> { entry ->
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

    composable<BusinessCustomerAdd> {
        CustomerAddScreen(onBack = back, onDone = back)
    }

    composable<BusinessCustomerView> { entry ->
        val args = entry.toRoute<BusinessCustomerView>()
        CustomerViewScreen(
            onBack = back,
            onEditClick = { id -> nav.safeNavigate(BusinessCustomerAdd(args.businessId, id)) },
            onDeleted = back,
        )
    }
}
// ── Más: administración, información y salida del negocio ──
private fun NavGraphBuilder.businessMore(nav: NavController) {
    val back = nav.backAction()

    composable<BusinessMore> { entry ->
        val args = entry.toRoute<BusinessMore>()
        BusinessMoreScreen(
            onTeamClick = { nav.safeNavigate(BusinessMembers(args.businessId)) },
            onCatalogClick = { nav.safeNavigate(BusinessCatalog(args.businessId)) },
            onHistoryClick = { nav.safeNavigate(BusinessHistory(args.businessId)) },
            onSettingsClick = { nav.safeNavigate(BusinessSettings(args.businessId)) },
            onBack = back,
            // TODO: Ayuda y Acerca de (rutas aún no definidas).
            onHelpClick = {},
//            onAboutClick = {},
//            // TODO: confirmar antes con ArxAlertDialog(isDestructive = true).
//            onLogoutClick = {
//                nav.navigate(MyBusinesses) {
//                    popUpTo<BusinessHome> { inclusive = true }
//                }
//            }
        )
    }

    composable<BusinessSettings> {
        PlaceholderScreen("Ajustes del negocio", back)
    }
    composable<BusinessCatalog> {
        BusinessCatalogScreen(
            onBackClick = back,
        )
    }
    composable<BusinessHistory> { PlaceholderScreen("Historial del negocio", back) }
}