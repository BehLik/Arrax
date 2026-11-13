package com.example.arrax.domain.usecase.order.create

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.EventStatus
import com.example.arrax.domain.model.Order
import com.example.arrax.domain.model.OrderLineInput
import com.example.arrax.domain.repository.CustomerRepository
import com.example.arrax.domain.repository.EventRepository
import com.example.arrax.domain.repository.OrderRepository
import com.example.arrax.domain.usecase.order.buildOrderItems
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlin.coroutines.cancellation.CancellationException

class CreateOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val eventRepository: EventRepository,
    private val customerRepository: CustomerRepository,
) {
    suspend operator fun invoke(
        tenantId: String,
        eventId: String,
        customerId: String,
        notes: String,
        lines: List<OrderLineInput>,
    ): Resource<String> {
        return try {
            require(lines.isNotEmpty()) { "El pedido debe tener al menos un corte" }

            val eventWithCuts = eventRepository.observeEvent(tenantId, eventId).first()
                ?: return Resource.Error("Evento no encontrado")
            require(eventWithCuts.event.status == EventStatus.OPEN) { "El evento está cerrado" }

            val customer = customerRepository.observeCustomer(tenantId, customerId).first()
                ?: return Resource.Error("Cliente no encontrado")

            val orderId = orderRepository.newOrderId(tenantId, eventId)
            val items = buildOrderItems(orderId, customerId, lines, eventWithCuts.cuts)

            orderRepository.createOrder(
                tenantId, eventId,
                Order(
                    id = orderId, customerId = customerId,
                    customerName = customer.name, notes = notes
                ),
                items,
            )
            Resource.Success(orderId)
        } catch (e: CancellationException) {
            throw e
        } catch (e: IllegalArgumentException) {
            Resource.Error(e.message ?: "Pedido inválido")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "No se pudo crear el pedido")
        }
    }
}