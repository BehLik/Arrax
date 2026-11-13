package com.example.arrax.domain.model

data class OrderWithItems(val order: Order, val items: List<OrderItem>) {
    val totalCents: Long get() = OrderMath.customerTotalCents(items)       // peso real
    val estimatedCents: Long get() = OrderMath.customerEstimatedCents(items)
    val isEditable: Boolean get() = items.none { it.isLocked }
}