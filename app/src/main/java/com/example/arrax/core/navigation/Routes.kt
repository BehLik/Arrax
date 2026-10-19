package com.example.arrax.core.navigation

import kotlinx.serialization.Serializable

// ── Auth ──
@Serializable data object AuthGraph
@Serializable data object Login
@Serializable data object Register
@Serializable data object ForgotPassword

// ── Lobby (alcance usuario, sin negocio) ──
@Serializable data object LobbyGraph
@Serializable data object LobbyHome
@Serializable data object MyBusinesses            // "Ver todos"
@Serializable data object BusinessCreate
@Serializable data object BusinessJoin
@Serializable data object Profile
@Serializable data object Settings
@Serializable data object Notifications

// ── Business (alcance negocio) ──
@Serializable data class BusinessHome(val businessId: String)
@Serializable data class BusinessSettings(val businessId: String)
@Serializable data class BusinessEvents(val businessId: String)

// Miembros
@Serializable data class BusinessMembers(val businessId: String)                           // lista
@Serializable data class BusinessMemberDetail(val businessId: String, val memberId: String)
@Serializable data class BusinessInvite(val businessId: String)                            // código de invitación

// Clientes
@Serializable data class BusinessCustomers(val businessId: String)                         // lista
@Serializable data class BusinessCustomerEdit(                                             // registrar / editar
    val businessId: String,
    val customerId: String? = null                                                          // null = nuevo
)
@Serializable data class BusinessCustomerDetail(val businessId: String, val customerId: String)

// ── Event (alcance evento) ──
@Serializable data class EventCreate(val businessId: String)
@Serializable data class EventHome(val businessId: String, val eventId: String)

// Pedidos
@Serializable data class EventOrders(val businessId: String, val eventId: String)          // lista (por cliente / por corte)
@Serializable data class EventOrderEdit(                                                   // registrar / editar
    val businessId: String,
    val eventId: String,
    val orderId: String? = null                                                             // null = nuevo
)
@Serializable data class EventOrderDetail(val businessId: String, val eventId: String, val orderId: String)

// Pesaje
@Serializable data class EventWeighingSetup(val businessId: String, val eventId: String)   // elegir modo
@Serializable data class EventWeighing(
    val businessId: String,
    val eventId: String,
    val mode: WeighingMode,
    val handsFree: Boolean = false
)

// Entregas y cobros
@Serializable data class EventSettlement(val businessId: String, val eventId: String)      // lista por cliente
@Serializable data class EventSettlementDetail(val businessId: String, val eventId: String, val customerId: String)

// Gastos y resultados
@Serializable data class EventExpenses(val businessId: String, val eventId: String)
@Serializable data class EventResults(val businessId: String, val eventId: String)

// ── Argumentos tipados ──
@Serializable
enum class WeighingMode { BY_CUT, BY_CUSTOMER }