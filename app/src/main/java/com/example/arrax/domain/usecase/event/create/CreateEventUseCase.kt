package com.example.arrax.domain.usecase.event.create

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.Event
import com.example.arrax.domain.model.EventCut
import com.example.arrax.domain.repository.EventRepository
import javax.inject.Inject


class CreateEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(tenantId: String, event: Event, cuts: List<EventCut>): Resource<String> {
        if (tenantId.isBlank()) {
            return Resource.Error("Falta el negocio (tenant) del evento")
        }
        if (event.name.isBlank()) {
            return Resource.Error("El evento necesita un nombre")
        }
        if (event.pigCount <= 0) {
            return Resource.Error("La cantidad de cerdos debe ser mayor a cero")
        }
        if (cuts.isEmpty()) {
            return Resource.Error("Selecciona al menos un corte")
        }
        val sinPrecio = cuts.firstOrNull { it.pricePerKg <= 0.0 }
        if (sinPrecio != null) {
            return Resource.Error("Todos los cortes seleccionados necesitan un precio mayor a cero")
        }
        return eventRepository.createEventWithCuts(tenantId, event, cuts)
    }
}
