package com.example.arrax.domain.repository

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.Event
import com.example.arrax.domain.model.EventCut
import com.example.arrax.domain.model.EventWithCuts
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    // FIX: tenantId ya no vive en Event (tu modelo real no lo tiene). Se pasa
    // explicito, igual que en observeEvents/observeEvent/closeEvent.
    suspend fun createEventWithCuts(tenantId: String, event: Event, cuts: List<EventCut>): Resource<String>

    fun observeEvents(tenantId: String): Flow<List<Event>>

    fun observeEvent(tenantId: String, eventId: String): Flow<EventWithCuts?>

    suspend fun closeEvent(tenantId: String, eventId: String): Resource<Unit>
}
