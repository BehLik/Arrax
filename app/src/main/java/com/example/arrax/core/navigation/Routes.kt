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
@Serializable data object Profile
@Serializable data object Settings
@Serializable data object Notifications
@Serializable data object BusinessCreate
@Serializable data object BusinessJoin

// ── Business (alcance negocio) ──
@Serializable data class BusinessHome(val businessId: String)
@Serializable data class BusinessMembers(val businessId: String)
@Serializable data class BusinessCustomers(val businessId: String)
@Serializable data class BusinessEvents(val businessId: String)

// ── Event (alcance evento) ──
@Serializable data class EventCreate(val businessId: String)
@Serializable data class EventHome(val businessId: String, val eventId: String)
@Serializable data class EventOrders(val businessId: String, val eventId: String)
@Serializable data class EventOrderCreate(val businessId: String, val eventId: String)
@Serializable data class EventOrderDetail(val businessId: String, val eventId: String, val orderId: String)
@Serializable data class EventWeighing(val businessId: String, val eventId: String, val mode: String)
@Serializable data class EventSettlement(val businessId: String, val eventId: String)
@Serializable data class EventSettlementDetail(val businessId: String, val eventId: String, val customerId: String)
@Serializable data class EventExpenses(val businessId: String, val eventId: String)
@Serializable data class EventResults(val businessId: String, val eventId: String)