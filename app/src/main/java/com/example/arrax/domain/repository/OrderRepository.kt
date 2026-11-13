package com.example.arrax.domain.repository

import com.example.arrax.domain.model.Order
import com.example.arrax.domain.model.OrderItem
import com.example.arrax.domain.model.WeighingStatus
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun newOrderId(tenantId: String, eventId: String): String
    fun createOrder(tenantId: String, eventId: String, order: Order, items: List<OrderItem>)

    fun observeOrders(tenantId: String, eventId: String): Flow<List<Order>>
    fun observeItems(tenantId: String, eventId: String): Flow<List<OrderItem>>

    fun confirmWeighing(
        tenantId: String, eventId: String, itemId: String,
        weighedGrams: Int, status: WeighingStatus
    )
    fun markDelivered(tenantId: String, eventId: String, itemIds: List<String>, deliveredAt: Long?)
    fun deleteOrder(tenantId: String, eventId: String, orderId: String, itemIds: List<String>)

    fun updateOrder(
        tenantId: String, eventId: String, orderId: String, notes: String,
        addedItems: List<OrderItem>, updatedItems: List<OrderItem>, removedItemIds: Set<String>,
    )
}