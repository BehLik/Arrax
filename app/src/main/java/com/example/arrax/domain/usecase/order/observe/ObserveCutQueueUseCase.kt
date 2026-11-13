package com.example.arrax.domain.usecase.order.observe

import com.example.arrax.domain.model.CutQueueEntry
import com.example.arrax.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import kotlin.text.orEmpty

class ObserveCutQueueUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
) {
    operator fun invoke(tenantId: String, eventId: String, cutId: String): Flow<List<CutQueueEntry>> =
        combine(
            orderRepository.observeOrders(tenantId, eventId),
            orderRepository.observeItems(tenantId, eventId),
        ) { orders, items ->
            val names = orders.associate { it.id to it.customerName }
            items.filter { it.cutId == cutId }
                .sortedWith(compareBy({ it.createdAt }, { it.id }))
                .map { CutQueueEntry(it, names[it.orderId].orEmpty()) }
        }
}