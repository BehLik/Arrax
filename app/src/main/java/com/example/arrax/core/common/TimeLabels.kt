package com.example.arrax.core.common

fun agoLabel(at: Long, now: Long): String {
    val minutes = ((now - at) / 60_000).coerceAtLeast(0)
    val hours = minutes / 60
    val days = hours / 24
    return when {
        minutes < 1 -> "Ahora"
        minutes < 60 -> "Hace $minutes min"
        hours < 24 -> "Hace $hours h"
        days == 1L -> "Ayer"
        else -> "Hace $days días"
    }
}