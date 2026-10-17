package com.example.arrax.data.mapper

import com.example.arrax.domain.model.Role
import com.example.arrax.domain.model.User
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toUser(): User? {
    if (!exists()) return null
    return User(
        uid = id,
        name = getString("name") ?: "",
        // null = user without a business (User.tenantId is String?), never ""
        tenantId = getString("tenantId")?.takeIf { it.isNotBlank() },
        role = getString("role").toRole()
    )
}

/** Unknown or missing role must NEVER fall back to ADMIN. "OPERADOR" is legacy data. */
internal fun String?.toRole(): Role = when (this?.uppercase()) {
    "ADMIN" -> Role.ADMIN
    "OPERATOR", "OPERADOR" -> Role.OPERATOR
    else -> Role.OPERATOR
}
