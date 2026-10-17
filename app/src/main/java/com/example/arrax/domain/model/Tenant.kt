package com.example.arrax.domain.model

data class Tenant(
    val id: String = "",
    val name: String = "",
    val createdAt: Long = 0L
)

//data class BusinessSummary(
//    val tenant: Tenant,
//    val isAdmin: Boolean,      // was: esAdmin
//    val totalEvents: Int       // was: totalEventos
//)
