package com.example.arrax.data.mapper

import com.example.arrax.data.remote.EventDto
import com.example.arrax.domain.model.Event
import com.example.arrax.domain.model.EventStatus

fun EventDto.toDomain(): Event = Event(
    id = id,
    name = name,
    date = date,
    pigCount = pigCount,
    status = runCatching { EventStatus.valueOf(status) }.getOrDefault(EventStatus.OPEN),
    createdBy = createdBy
)

fun Event.toDto(): EventDto = EventDto(
    id = id,
    name = name,
    date = date,
    pigCount = pigCount,
    status = status.name,
    createdBy = createdBy
)
