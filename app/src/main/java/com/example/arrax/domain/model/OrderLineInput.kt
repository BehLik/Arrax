package com.example.arrax.domain.model

data class OrderLineInput(
    val cutId: String,
    val requestedBy: RequestMode,
    val requestedGrams: Int? = null,
    val requestedAmountCents: Long? = null,
)