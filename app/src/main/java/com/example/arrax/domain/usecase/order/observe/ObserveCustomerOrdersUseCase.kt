package com.example.arrax.domain.usecase.order.observe

import com.example.arrax.domain.model.CustomerOrders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2

class ObserveCustomerOrdersUseCase @Inject constructor(
    private val observeOrders: ObserveOrdersUseCase,
) {
    operator fun invoke(tenantId: String, eventId: String): Flow<List<CustomerOrders>> =
        observeOrders(tenantId, eventId).map { list ->
            list.groupBy { it.order.customerId }
                .map { (customerId, orders) ->
                    CustomerOrders(customerId, orders.first().order.customerName, orders)
                }
                .sortedBy { it.customerName.lowercase() }
        }
}