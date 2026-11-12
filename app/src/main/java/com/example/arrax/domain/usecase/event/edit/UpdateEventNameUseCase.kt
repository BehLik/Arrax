package com.example.arrax.domain.usecase.event.edit

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.repository.EventRepository
import javax.inject.Inject


class UpdateEventNameUseCase @Inject constructor(private val repo: EventRepository) {
    suspend operator fun invoke(tenantId: String, eventId: String, name: String): Resource<Unit> {
        if (name.isBlank()) return Resource.Error("El evento necesita un nombre")
        return repo.updateEventName(tenantId, eventId, name.trim())
    }
}