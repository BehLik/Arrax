package com.example.arrax.domain.model

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val tenantId: String? = null,
    val role: Role = Role.OPERATOR,
    val photoUrl: String? = null
)
