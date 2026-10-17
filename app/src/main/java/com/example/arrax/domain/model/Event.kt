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

/** Money is stored in integer cents, never floating point. */
//data class EventCut(
//    val cutId: String = "",
//    val name: String = "",
//    val pricePerKgCents: Long = 0L   // was: pricePerKg: Double
//)

//data class EventWithCuts(
//    val event: Event,
//    val cuts: List<EventCut> = emptyList()
//)
