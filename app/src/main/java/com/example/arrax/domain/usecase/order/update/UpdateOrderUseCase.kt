package com.example.arrax.domain.usecase.order.update

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.EventStatus
import com.example.arrax.domain.model.OrderLineInput
import com.example.arrax.domain.repository.EventRepository
import com.example.arrax.domain.repository.OrderRepository
import com.example.arrax.domain.usecase.order.buildOrderItems
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlin.coroutines.cancellation.CancellationException

class UpdateOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val eventRepository: EventRepository,
) {
    suspend operator fun invoke(
        tenantId: String,
        eventId: String,
        orderId: String,
        notes: String,
        lines: List<OrderLineInput>,
    ): Resource<Unit> {
        return try {
            val eventWithCuts = eventRepository.observeEvent(tenantId, eventId).first()
                ?: return Resource.Error("Evento no encontrado")
            require(eventWithCuts.event.status == EventStatus.OPEN) { "El evento está cerrado" }

            val order = orderRepository.observeOrders(tenantId, eventId).first()
                .firstOrNull { it.id == orderId }
                ?: return Resource.Error("Pedido no encontrado")
            val current = orderRepository.observeItems(tenantId, eventId).first()
                .filter { it.orderId == orderId }

            val locked = current.filter { it.isLocked }
            val editable = current.filterNot { it.isLocked }

            require(lines.none { l -> locked.any { it.cutId == l.cutId } }) {
                "Un corte ya se pesó y no se puede modificar"
            }
            require(lines.isNotEmpty() || locked.isNotEmpty()) {
                "El pedido debe tener al menos un corte"
            }

            val desired = buildOrderItems(orderId, order.customerId, lines, eventWithCuts.cuts)
            val currentIds = editable.map { it.id }.toSet()
            val desiredIds = desired.map { it.id }.toSet()

            orderRepository.updateOrder(
                tenantId, eventId, orderId, notes,
                addedItems = desired.filter { it.id !in currentIds },
                updatedItems = desired.filter { it.id in currentIds },
                removedItemIds = currentIds - desiredIds,
            )
            Resource.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: IllegalArgumentException) {
            Resource.Error(e.message ?: "Pedido inválido")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "No se pudo actualizar el pedido")
        }
    }
}