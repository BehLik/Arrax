package com.example.arrax.domain.usecase.event.create

import com.example.arrax.domain.model.Event
import com.example.arrax.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    operator fun invoke(tenantId: String): Flow<List<Event>> =
        eventRepository.observeEvents(tenantId)
}
