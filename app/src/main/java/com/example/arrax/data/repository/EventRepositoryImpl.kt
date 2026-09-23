package com.example.arrax.data.repository

import com.example.arrax.core.common.Resource
import com.example.arrax.data.mapper.toDomain
import com.example.arrax.data.mapper.toDto
import com.example.arrax.data.remote.FirestoreEventDataSource
import com.example.arrax.domain.model.Event
import com.example.arrax.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val eventDataSource: FirestoreEventDataSource
) : EventRepository {

    override fun observeEvents(tenantId: String): Flow<List<Event>> =
        eventDataSource.observeEvents(tenantId).map { list -> list.map { it.toDomain() } }

    override suspend fun createEvent(tenantId: String, event: Event): Resource<Event> = try {
        val dto = eventDataSource.createEvent(tenantId, event.toDto())
        Resource.Success(dto.toDomain())
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear el evento", e)
    }
}
