package com.example.arrax.data.repository

import com.example.arrax.core.common.Resource
import com.example.arrax.data.mapper.toEvent
import com.example.arrax.data.mapper.toEventCut
import com.example.arrax.data.mapper.toMap
import com.example.arrax.data.remote.FirestoreEventDataSource
import com.example.arrax.domain.model.Event
import com.example.arrax.domain.model.EventCut
import com.example.arrax.domain.model.EventWithCuts
import com.example.arrax.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class EventRepositoryImpl @Inject constructor(
    private val eventDataSource: FirestoreEventDataSource
) : EventRepository {

    override suspend fun createEventWithCuts(
        tenantId: String,
        event: Event,
        cuts: List<EventCut>
    ): Resource<String> {
        return try {
            val id = eventDataSource.createEventWithCuts(
                tenantId = tenantId,
                eventData = event.toMap(),
                cutsData = cuts.map { it.toMap() }
            )
            Resource.Success(id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "No se pudo crear el evento")
        }
    }

    override fun observeEvents(tenantId: String): Flow<List<Event>> {
        return eventDataSource.observeEvents(tenantId).map { docs ->
            docs.mapNotNull { it.toEvent() }
        }
    }

    override fun observeEvent(tenantId: String, eventId: String): Flow<EventWithCuts?> {
        return eventDataSource.observeEvent(tenantId, eventId).map { pair ->
            val eventDoc = pair.first
            val cutDocs = pair.second
            val event = eventDoc?.toEvent() ?: return@map null
            EventWithCuts(event = event, cuts = cutDocs.mapNotNull { it.toEventCut() })
        }
    }

    override suspend fun closeEvent(tenantId: String, eventId: String): Resource<Unit> {
        return try {
            eventDataSource.closeEvent(tenantId, eventId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "No se pudo cerrar el evento")
        }
    }
}
