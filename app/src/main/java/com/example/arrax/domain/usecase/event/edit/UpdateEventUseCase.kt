package com.example.arrax.domain.usecase.event.edit

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.EventCut
import com.example.arrax.domain.repository.EventRepository
import javax.inject.Inject

class UpdateEventUseCase @Inject constructor(private val repo: EventRepository) {
    suspend operator fun invoke(
        tenantId: String, eventId: String, name: String, date: Long, animalCount: Int,
        cuts: List<EventCut>, removedCutIds: List<String>
    ): Resource<Unit> {
        if (name.isBlank()) return Resource.Error("El evento necesita un nombre")
        if (animalCount <= 0) return Resource.Error("La cantidad de cerdos debe ser mayor a cero")
        if (cuts.isEmpty()) return Resource.Error("Selecciona al menos un corte")
        if (cuts.any { it.pricePerKgCents <= 0L })
            return Resource.Error("Todos los cortes necesitan un precio mayor a cero")
        return repo.updateEventWithCuts(tenantId, eventId, name.trim(), date, animalCount, cuts, removedCutIds)
    }
}