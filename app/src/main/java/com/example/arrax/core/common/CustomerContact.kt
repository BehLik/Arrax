package com.example.arrax.core.common

import com.example.arrax.domain.model.Customer

fun Customer.fullPhone(): String? {
    val digits = phone.filter(Char::isDigit)
    return if (digits.isEmpty()) null else "+${countryCode.filter(Char::isDigit)}$digits"
}

fun Customer.fullAddress(): String =
    listOf(address, municipality, state, "México").filter { it.isNotBlank() }.joinToString(", ")