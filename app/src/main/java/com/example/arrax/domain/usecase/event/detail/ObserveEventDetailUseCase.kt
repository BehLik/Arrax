package com.example.arrax.domain.usecase.event.detail

import com.example.arrax.domain.model.EventWithCuts
import com.example.arrax.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveEventDetailUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    operator fun invoke(tenantId: String, eventId: String): Flow<EventWithCuts?> {
        return eventRepository.observeEvent(tenantId, eventId)
    }
}