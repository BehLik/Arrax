package com.example.arrax.domain.model

enum class RequestMode { KG, MONEY }
enum class WeighingStatus { PENDING, COMPLETE, SHORT, UNFULFILLED }

data class Order(
    val id: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val notes: String = "",
    val createdAt: Long = 0L,
)

data class OrderItem(
    val id: String = "",
    val orderId: String = "",
    val customerId: String = "",
    val cutId: String = "",
    val cutName: String = "",
    val pricePerKgCents: Long = 0L,
    val requestedBy: RequestMode = RequestMode.KG,
    val requestedGrams: Int? = null,
    val requestedAmountCents: Long? = null,
    val targetGrams: Int = 0,
    val weighedGrams: Int = 0,
    val weighingStatus: WeighingStatus = WeighingStatus.PENDING,
    val deliveredAt: Long? = null,
    val createdAt: Long = 0L,
)