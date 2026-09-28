package com.example.arrax.domain.usecase.event.detail


import com.example.arrax.core.common.Resource
import com.example.arrax.domain.repository.EventRepository
import javax.inject.Inject

class CloseEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(tenantId: String, eventId: String): Resource<Unit> {
        return eventRepository.closeEvent(tenantId, eventId)
    }
}
