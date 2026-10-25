package com.example.arrax.ui.screen.business

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.arrax.core.navigation.*
import com.example.arrax.ui.root.navigation.PlaceholderScreen
import com.example.arrax.ui.root.navigation.backAction
import com.example.arrax.ui.root.navigation.safeNavigate
import com.example.arrax.ui.screen.business.alerts.BusinessAlertsScreen
import com.example.arrax.ui.screen.business.customers.BusinessCustomersScreen
import com.example.arrax.ui.screen.business.home.BusinessHomeScreen
import com.example.arrax.ui.screen.business.members.BusinessMembersScreen
import com.example.arrax.ui.screen.business.more.BusinessMoreScreen

fun NavGraphBuilder.businessGraph(nav: NavController) {
    businessCore(nav)
    businessAlerts(nav)
    businessMembers(nav)
    businessCustomers(nav)
    businessMore(nav)
}

// ── Inicio y eventos del negocio ──
private fun NavGraphBuilder.businessCore(nav: NavController) {
    val back = nav.backAction()

    composable<BusinessHome> { entry ->
        val args = entry.toRoute<BusinessHome>()
        BusinessHomeScreen(
            onBack = back,
            onCreateEventClick = { nav.safeNavigate(EventCreate(args.businessId)) },
            // TODO: cuando onEventClick reciba el id -> nav.safeNavigate(EventHome(args.businessId, eventId))
            onEventClick = { nav.safeNavigate(BusinessEvents(args.businessId)) },
            // Tocar el negocio activo lleva a la lista de negocios (cambiar de negocio).
            onBusinessClick = { nav.safeNavigate(MyBusinesses) },
            onNotificationsClick = { nav.safeNavigate(Notifications) },
            onAlertsClick = { nav.safeNavigate(BusinessAlerts(args.businessId)) },
            onCustomersClick = { nav.safeNavigate(BusinessCustomers(args.businessId)) },
            onTeamClick = { nav.safeNavigate(BusinessMembers(args.businessId)) },
            onCutsClick = { nav.safeNavigate(BusinessCatalog(args.businessId)) }
        )
    }

    composable<BusinessEvents> {
        // TODO: reemplazar por la Screen de eventos (Próximos / Abiertos / Historial).
        PlaceholderScreen("Eventos del negocio", back)
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

    composable<BusinessMembers> { entry ->
        val args = entry.toRoute<BusinessMembers>()
        BusinessMembersScreen(
            onBack = back,
            onMemberClick = { id -> nav.safeNavigate(BusinessMemberDetail(args.businessId, id)) }
            // TODO: onShareQr / onShareLink -> Intent.ACTION_SEND desde la Activity o un helper.
        )
    }

    composable<BusinessMemberDetail> { PlaceholderScreen("Detalle del miembro", back) }

    // Reemplazada por el bottom sheet de BusinessMembersScreen. Borrar si nada navega aquí.
    composable<BusinessInvite> { PlaceholderScreen("Invitar al negocio", back) }
}

// ── Clientes: lista, crear/editar y detalle ──
private fun NavGraphBuilder.businessCustomers(nav: NavController) {
    val back = nav.backAction()

    composable<BusinessCustomers> { entry ->
        val args = entry.toRoute<BusinessCustomers>()
        BusinessCustomersScreen(
            onAddCustomerClick = { nav.safeNavigate(BusinessCustomerEdit(args.businessId)) },
            onCustomerClick = { id -> nav.safeNavigate(BusinessCustomerDetail(args.businessId, id)) }
        )
    }

    composable<BusinessCustomerEdit> { entry ->
        val args = entry.toRoute<BusinessCustomerEdit>()
        // TODO: al guardar -> nav.safePopBackStack()
        PlaceholderScreen(
            title = if (args.customerId == null) "Nuevo cliente" else "Editar cliente",
            onBack = back
        )
    }

    composable<BusinessCustomerDetail> { PlaceholderScreen("Detalle del cliente", back) }
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
            // TODO: Ayuda y Acerca de (rutas aún no definidas).
            onHelpClick = {},
            onAboutClick = {},
            // TODO: confirmar antes con ArxAlertDialog(isDestructive = true).
            onLogoutClick = {
                nav.navigate(MyBusinesses) {
                    popUpTo<BusinessHome> { inclusive = true }
                }
            }
        )
    }

    composable<BusinessSettings> { PlaceholderScreen("Ajustes del negocio", back) }
    composable<BusinessCatalog> { PlaceholderScreen("Catálogo de productos y cortes", back) }
    composable<BusinessHistory> { PlaceholderScreen("Historial del negocio", back) }
}