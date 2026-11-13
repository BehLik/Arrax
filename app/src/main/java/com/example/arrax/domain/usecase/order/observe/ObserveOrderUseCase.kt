package com.example.arrax.domain.usecase.order.observe

import com.example.arrax.domain.model.OrderWithItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveOrderUseCase @Inject constructor(
    private val observeOrders: ObserveOrdersUseCase,
) {
    operator fun invoke(tenantId: String, eventId: String, orderId: String): Flow<OrderWithItems?> =
        observeOrders(tenantId, eventId).map { list -> list.firstOrNull { it.order.id == orderId } }
}