package com.example.arrax.domain.model

data class BusinessSummary(
    val tenant: Tenant,
    val role: Role,
    val totalEvents: Int
) {
    val isAdmin: Boolean get() = role == Role.OWNER || role == Role.ADMIN
}