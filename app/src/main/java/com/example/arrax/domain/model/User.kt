package com.example.arrax.domain.model

enum class Role { ADMIN, OPERADOR }

data class User(
    val uid: String = "",
    val name: String = "",
    val tenantId: String? = null,
    val role: Role = Role.OPERADOR
)
