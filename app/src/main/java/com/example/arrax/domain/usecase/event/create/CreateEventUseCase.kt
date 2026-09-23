package com.example.arrax.domain.usecase.event.create


import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.Event
import com.example.arrax.domain.repository.EventRepository
import javax.inject.Inject

class CreateEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(tenantId: String, event: Event): Resource<Event> {
        if (event.name.isBlank()) return Resource.Error("El nombre del evento es obligatorio")
        if (event.pigCount <= 0) return Resource.Error("La cantidad de cerdos debe ser mayor a 0")
        return eventRepository.createEvent(tenantId, event)
    }
}
