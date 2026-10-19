package com.example.arrax.ui.screen.business

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.arrax.core.navigation.*
import com.example.arrax.ui.root.navigation.PlaceholderAction
import com.example.arrax.ui.root.navigation.PlaceholderScreen
import com.example.arrax.ui.root.navigation.backAction
import com.example.arrax.ui.root.navigation.safeNavigate

fun NavGraphBuilder.businessGraph(nav: NavController) {
    businessCore(nav)
    businessMembers(nav)
    businessCustomers(nav)
}

// ── Inicio, ajustes y eventos del negocio ──
private fun NavGraphBuilder.businessCore(nav: NavController) {
    val back = nav.backAction()

    composable<BusinessHome> { entry ->
        val args = entry.toRoute<BusinessHome>()
        // TODO: BusinessHomeScreen(onBack = back, onCreateEventClick = ..., onEventClick = ...)
        PlaceholderScreen(
            title = "Negocio",
            onBack = back,
            actions = listOf(
                PlaceholderAction("Nuevo evento") { nav.safeNavigate(EventCreate(args.businessId)) },
                PlaceholderAction("Eventos") { nav.safeNavigate(BusinessEvents(args.businessId)) },
                PlaceholderAction("Clientes") { nav.safeNavigate(BusinessCustomers(args.businessId)) },
                PlaceholderAction("Miembros") { nav.safeNavigate(BusinessMembers(args.businessId)) },
                PlaceholderAction("Ajustes") { nav.safeNavigate(BusinessSettings(args.businessId)) }
            )
        )
    }

    composable<BusinessSettings> { PlaceholderScreen("Ajustes del negocio", back) }

    composable<BusinessEvents> {
        // TODO: onEventClick = { eventId -> nav.safeNavigate(EventHome(args.businessId, eventId)) }
        PlaceholderScreen("Eventos del negocio", back)
    }
}

// ── Miembros: lista, detalle e invitación ──
private fun NavGraphBuilder.businessMembers(nav: NavController) {
    val back = nav.backAction()

    composable<BusinessMembers> { entry ->
        val args = entry.toRoute<BusinessMembers>()
        // TODO: onMemberClick = { id -> nav.safeNavigate(BusinessMemberDetail(args.businessId, id)) }
        PlaceholderScreen(
            title = "Miembros",
            onBack = back,
            actions = listOf(
                PlaceholderAction("Invitar") { nav.safeNavigate(BusinessInvite(args.businessId)) }
            )
        )
    }

    composable<BusinessMemberDetail> { PlaceholderScreen("Detalle del miembro", back) }
    composable<BusinessInvite> { PlaceholderScreen("Invitar al negocio", back) }
}

// ── Clientes: lista, crear/editar y detalle ──
private fun NavGraphBuilder.businessCustomers(nav: NavController) {
    val back = nav.backAction()

    composable<BusinessCustomers> { entry ->
        val args = entry.toRoute<BusinessCustomers>()
        // TODO: onCustomerClick = { id -> nav.safeNavigate(BusinessCustomerDetail(args.businessId, id)) }
        PlaceholderScreen(
            title = "Clientes",
            onBack = back,
            actions = listOf(
                PlaceholderAction("Nuevo cliente") { nav.safeNavigate(BusinessCustomerEdit(args.businessId)) }
            )
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

    composable<BusinessCustomerDetail> { entry ->
        val args = entry.toRoute<BusinessCustomerDetail>()
        PlaceholderScreen(
            title = "Detalle del cliente",
            onBack = back,
            actions = listOf(
                PlaceholderAction("Editar") {
                    nav.safeNavigate(BusinessCustomerEdit(args.businessId, args.customerId))
                }
            )
        )
    }
}