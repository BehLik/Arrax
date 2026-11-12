package com.example.arrax.domain.repository

import com.example.arrax.domain.model.Order
import com.example.arrax.domain.model.OrderItem
import com.example.arrax.domain.model.WeighingStatus
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun newOrderId(tenantId: String, eventId: String): String
    fun createOrder(tenantId: String, eventId: String, order: Order, items: List<OrderItem>)
    fun deleteOrder(tenantId: String, eventId: String, orderId: String)

    fun observeOrders(tenantId: String, eventId: String): Flow<List<Order>>
    fun observeItems(tenantId: String, eventId: String): Flow<List<OrderItem>>

    fun confirmWeighing(
        tenantId: String, eventId: String, itemId: String,
        weighedGrams: Int, status: WeighingStatus
    )
    fun markDelivered(tenantId: String, eventId: String, itemIds: List<String>, deliveredAt: Long?)
}