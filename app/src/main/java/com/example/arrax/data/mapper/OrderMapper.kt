package com.example.arrax.data.mapper

import com.example.arrax.data.remote.OrderDto
import com.example.arrax.data.remote.OrderItemDto
import com.example.arrax.domain.model.Order
import com.example.arrax.domain.model.OrderItem
import com.example.arrax.domain.model.RequestMode
import com.example.arrax.domain.model.WeighingStatus


fun OrderDto.toDomain() = Order(id, customerId, customerName, notes, createdAt)
fun Order.toDto() = OrderDto(id = id, customerId = customerId,
    customerName = customerName, notes = notes.trim())

fun OrderItemDto.toDomain() = OrderItem(
    id = id, orderId = orderId, customerId = customerId, cutId = cutId, cutName = cutName,
    pricePerKgCents = pricePerKgCents,
    requestedBy = runCatching { RequestMode.valueOf(requestedBy) }.getOrDefault(RequestMode.KG),
    requestedGrams = requestedGrams, requestedAmountCents = requestedAmountCents,
    targetGrams = targetGrams, weighedGrams = weighedGrams,
    weighingStatus = runCatching { WeighingStatus.valueOf(weighingStatus) }
        .getOrDefault(WeighingStatus.PENDING),
    deliveredAt = deliveredAt, createdAt = createdAt,
)

fun OrderItem.toDto() = OrderItemDto(
    id = id, orderId = orderId, customerId = customerId, cutId = cutId, cutName = cutName,
    pricePerKgCents = pricePerKgCents, requestedBy = requestedBy.name,
    requestedGrams = requestedGrams, requestedAmountCents = requestedAmountCents,
    targetGrams = targetGrams,
)