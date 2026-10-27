package com.example.arrax.domain.model

data class Invitation(
    val code: String,
    val tenantId: String,
    val tenantName: String,
    val role: Role,
    val expiresAt: Long
)