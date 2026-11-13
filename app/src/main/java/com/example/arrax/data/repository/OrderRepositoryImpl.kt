package com.example.arrax.data.repository

import com.example.arrax.data.mapper.toDomain
import com.example.arrax.data.mapper.toDto
import com.example.arrax.data.remote.FirestoreOrderDataSource
import com.example.arrax.domain.model.Order
import com.example.arrax.domain.model.OrderItem
import com.example.arrax.domain.model.WeighingStatus
import com.example.arrax.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val ds: FirestoreOrderDataSource
) : OrderRepository {

    override fun newOrderId(tenantId: String, eventId: String) = ds.newId(tenantId, eventId)

    override fun createOrder(tenantId: String, eventId: String, order: Order, items: List<OrderItem>) =
        ds.createOrder(tenantId, eventId, order.toDto(), items.map { it.toDto() })

    override fun deleteOrder(
        tenantId: String, eventId: String, orderId: String, itemIds: List<String>
    ) = ds.deleteOrder(tenantId, eventId, orderId, itemIds)

    override fun updateOrder(
        tenantId: String, eventId: String, orderId: String, notes: String,
        addedItems: List<OrderItem>, updatedItems: List<OrderItem>, removedItemIds: Set<String>
    ) = ds.updateOrder(
        tenantId, eventId, orderId, notes,
        added = addedItems.map { it.toDto() },
        updated = updatedItems.map { it.toDto() },
        removedIds = removedItemIds
    )

    override fun observeOrders(tenantId: String, eventId: String): Flow<List<Order>> =
        ds.observeOrders(tenantId, eventId).map { l -> l.map { it.toDomain() }.sortedBy { it.createdAt } }

    override fun observeItems(tenantId: String, eventId: String): Flow<List<OrderItem>> =
        ds.observeItems(tenantId, eventId).map { l ->
            l.map { it.toDomain() }.sortedWith(compareBy({ it.createdAt }, { it.id }))
        }

    override fun confirmWeighing(
        tenantId: String, eventId: String, itemId: String,
        weighedGrams: Int, status: WeighingStatus
    ) = ds.confirmWeighing(tenantId, eventId, itemId, weighedGrams, status.name)

    override fun markDelivered(tenantId: String, eventId: String, itemIds: List<String>, deliveredAt: Long?) =
        ds.markDelivered(tenantId, eventId, itemIds, deliveredAt)
}