package com.example.arrax.domain.model

enum class EventStatus { OPEN, CLOSED }

data class Event(
    val id: String = "",
    val name: String = "",
    val date: Long = 0L,
    val pigCount: Int = 0,
    val status: EventStatus = EventStatus.OPEN,
    val createdBy: String = ""
)
