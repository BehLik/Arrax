package com.example.arrax.data.mapper

import com.example.arrax.data.remote.CustomerDto
import com.example.arrax.domain.model.Customer

fun CustomerDto.toDomain() = Customer(
    id = id,
    name = name,
    phone = phone.orEmpty(),
    address = address.orEmpty(),
    reference = reference.orEmpty(),
    photoUrl = photoUrl,
    createdAt = createdAt,
)

fun Customer.toDto() = CustomerDto(
    id = id,
    name = name.trim(),
    phone = phone.trim().ifBlank { null },
    address = address.trim().ifBlank { null },
    reference = reference.trim().ifBlank { null },
    photoUrl = photoUrl,
)