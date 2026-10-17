package com.example.arrax.domain.model

data class BusinessSummary(
    val tenant: Tenant,
    val esAdmin: Boolean,
    val totalEventos: Int
)