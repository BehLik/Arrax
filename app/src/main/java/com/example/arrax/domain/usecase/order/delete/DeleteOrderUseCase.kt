package com.example.arrax.domain.usecase.order.delete

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.EventStatus
import com.example.arrax.domain.repository.EventRepository
import com.example.arrax.domain.repository.OrderRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlin.coroutines.cancellation.CancellationException

class DeleteOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val eventRepository: EventRepository,
) {
    suspend operator fun invoke(
        tenantId: String, eventId: String, orderId: String
    ): Resource<Unit> {
        return try {
            val eventWithCuts = eventRepository.observeEvent(tenantId, eventId).first()
                ?: return Resource.Error("Evento no encontrado")
            require(eventWithCuts.event.status == EventStatus.OPEN) { "El evento está cerrado" }

            val items = orderRepository.observeItems(tenantId, eventId).first()
                .filter { it.orderId == orderId }
            require(items.none { it.isLocked }) {
                "El pedido ya tiene cortes pesados o entregados y no se puede eliminar"
            }

            orderRepository.deleteOrder(tenantId, eventId, orderId, items.map { it.id })
            Resource.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: IllegalArgumentException) {
            Resource.Error(e.message ?: "No se puede eliminar")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "No se pudo eliminar el pedido")
        }
    }
}