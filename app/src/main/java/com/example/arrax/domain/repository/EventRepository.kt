package com.example.arrax.domain.repository

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun observeEvents(tenantId: String): Flow<List<Event>>
    suspend fun createEvent(tenantId: String, event: Event): Resource<Event>
}
