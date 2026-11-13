package com.example.arrax.domain.usecase.order.observe

import com.example.arrax.domain.model.CustomerOrders
import com.example.arrax.domain.model.CutQueueEntry
import com.example.arrax.domain.model.OrderWithItems
import com.example.arrax.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
) {
    operator fun invoke(tenantId: String, eventId: String): Flow<List<OrderWithItems>> =
        combine(
            orderRepository.observeOrders(tenantId, eventId),
            orderRepository.observeItems(tenantId, eventId),
        ) { orders, items ->
            val byOrder = items.groupBy { it.orderId }
            orders.map { OrderWithItems(it, byOrder[it.id].orEmpty()) }
        }
}
