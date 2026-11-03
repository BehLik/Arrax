package com.example.arrax.domain.model

data class Customer(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val reference: String = "",
    val photoUrl: String? = null,
)