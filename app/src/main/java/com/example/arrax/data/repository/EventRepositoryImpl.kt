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
import kotlinx.coroutines.CancellationException
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
        } catch (e: CancellationException) {
            throw e // never swallow coroutine cancellation
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
    override suspend fun updateEventName(tenantId: String, eventId: String, name: String): Resource<Unit> =
        try {
            eventDataSource.updateEventName(tenantId, eventId, name)
            Resource.Success(Unit)
        } catch (e: CancellationException) { throw e }
        catch (e: Exception) { Resource.Error(e.message ?: "No se pudo actualizar el nombre") }

    override suspend fun updateEventWithCuts(
        tenantId: String, eventId: String, name: String, date: Long, animalCount: Int,
        cuts: List<EventCut>, removedCutIds: List<String>
    ): Resource<Unit> =
        try {
            eventDataSource.updateEventWithCuts(
                tenantId, eventId,
                eventData = mapOf("name" to name, "date" to date, "animalCount" to animalCount),
                cutsData = cuts.map { it.toMap() },
                removedCutIds = removedCutIds
            )
            Resource.Success(Unit)
        } catch (e: CancellationException) { throw e }
        catch (e: Exception) { Resource.Error(e.message ?: "No se pudo guardar el evento") }

    override suspend fun closeEvent(tenantId: String, eventId: String): Resource<Unit> {
        return try {
            eventDataSource.closeEvent(tenantId, eventId)
            Resource.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Resource.Error(e.message ?: "No se pudo cerrar el evento")
        }
    }
}
