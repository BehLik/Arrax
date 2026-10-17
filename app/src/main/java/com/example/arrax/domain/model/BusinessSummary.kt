package com.example.arrax.domain.model

data class BusinessSummary(
    val tenant: Tenant,
    val isAdmin: Boolean,      // was: esAdmin
    val totalEvents: Int       // was: totalEventos
)