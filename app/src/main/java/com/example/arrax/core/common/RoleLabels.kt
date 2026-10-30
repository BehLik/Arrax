package com.example.arrax.core.common

import com.example.arrax.domain.model.Role
import kotlin.math.abs

fun Role.toLabel() = when (this) {
    Role.OWNER -> "Propietario"
    Role.OPERATOR -> "Operador"
    else -> "Administrador"
}

fun expiryLabel(expiresAt: Long, now: Long): String {
    val diff = expiresAt - now
    val minutes = abs(diff) / 60_000
    val hours = minutes / 60
    val days = hours / 24
    val span = when {
        days >= 1 -> "$days ${if (days == 1L) "día" else "días"}"
        hours >= 1 -> "$hours h"
        else -> "${maxOf(minutes, 1)} min"
    }
    return if (diff > 0) "Expira en $span" else "Expiró hace $span"
}