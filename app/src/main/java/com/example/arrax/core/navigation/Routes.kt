package com.example.arrax.core.navigation

import kotlinx.serialization.Serializable

// ── Auth ──
@Serializable data object AuthGraph
@Serializable data object Login
@Serializable data object Register
@Serializable data object ForgotPassword

// ── Lobby (alcance usuario) ──
@Serializable data object LobbyGraph
@Serializable data object LobbyHome
@Serializable data object Profile
@Serializable data object Settings
@Serializable data object Notifications
@Serializable data object BusinessCreate
@Serializable data object BusinessJoin

// ── Business (alcance negocio) ──
@Serializable data class BusinessGraph(val businessId: String)
@Serializable data class BusinessHome(val businessId: String)

// ── Event (alcance evento) ──
@Serializable data class EventCreate(val businessId: String)
@Serializable data class EventDetail(val businessId: String, val eventId: String)