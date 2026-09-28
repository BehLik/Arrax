package com.example.arrax.data.mapper

import com.example.arrax.domain.model.Role
import com.google.firebase.firestore.DocumentSnapshot
import com.example.arrax.domain.model.User

fun DocumentSnapshot.toUser(): User? {
    if (!exists()) return null
    val roleName = getString("role")?.uppercase()
    val role = Role.entries.firstOrNull { it.name == roleName } ?: Role.entries.first()
    return User(
        uid = id,
        name = getString("name") ?: "",
        tenantId = getString("tenantId") ?: "",
        role = role
    )
}