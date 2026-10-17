package com.example.arrax.domain.model

/** Money is stored in integer cents, never floating point. */
data class EventCut(
    val cutId: String = "",
    val name: String = "",
    val pricePerKgCents: Long = 0L   // was: pricePerKg: Double
)
