package com.example.arrax.domain.model

data class CustomerOrders(
    val customerId: String,
    val customerName: String,
    val orders: List<OrderWithItems>,
) {
    val items: List<OrderItem> get() = orders.flatMap { it.items }
    val totalCents: Long get() = OrderMath.customerTotalCents(items)
    val estimatedCents: Long get() = OrderMath.customerEstimatedCents(items)
}