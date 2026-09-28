package com.example.arrax.domain.model


data class EventWithCuts(
    val event: Event,
    val cuts: List<EventCut> = emptyList()
)
