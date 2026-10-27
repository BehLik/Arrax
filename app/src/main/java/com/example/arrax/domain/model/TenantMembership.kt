package com.example.arrax.domain.model

data class TenantMembership(
    val tenant: Tenant,
    val role: Role,
    val eventCount: Int
)