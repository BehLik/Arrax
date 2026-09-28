package com.example.arrax.data.mapper

import com.example.arrax.data.remote.EventDto
import com.example.arrax.domain.model.Event
import com.example.arrax.domain.model.EventCut
import com.example.arrax.domain.model.EventStatus
import com.google.firebase.firestore.DocumentSnapshot


fun DocumentSnapshot.toEvent(): Event? {
    if (!exists()) return null
    val statusName = getString("status") ?: EventStatus.OPEN.name
    return Event(
        id = getString("id") ?: id,
        name = getString("name") ?: "",
        date = getLong("date") ?: 0L,
        pigCount = (getLong("pigCount") ?: 0L).toInt(),
        status = runCatching { EventStatus.valueOf(statusName) }.getOrDefault(EventStatus.OPEN),
        createdBy = getString("createdBy") ?: ""
    )
}

fun Event.toMap(): Map<String, Any> = mapOf(
    "id" to id,
    "name" to name,
    "date" to date,
    "pigCount" to pigCount,
    "status" to status.name,
    "createdBy" to createdBy
)

fun DocumentSnapshot.toEventCut(): EventCut? {
    if (!exists()) return null
    return EventCut(
        cutId = getString("cutId") ?: id,
        name = getString("name") ?: "",
        pricePerKg = getDouble("pricePerKg") ?: 0.0
    )
}

fun EventCut.toMap(): Map<String, Any> = mapOf(
    "cutId" to cutId,
    "name" to name,
    "pricePerKg" to pricePerKg
)