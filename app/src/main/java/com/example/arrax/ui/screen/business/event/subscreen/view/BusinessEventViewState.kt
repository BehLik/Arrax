package com.example.arrax.ui.screen.business.event.subscreen.view

import androidx.compose.runtime.Immutable
import com.example.arrax.domain.model.Event
import com.example.arrax.domain.model.EventStatus

enum class EventFilter(val label: String) { ALL("Todos"), OPEN("Abiertos"), CLOSED("Cerrados") }

@Immutable
data class BusinessEventViewState(
    val isLoading: Boolean = true,
    val tenantId: String? = null,
    val events: List<Event> = emptyList(),
    val filter: EventFilter = EventFilter.ALL,
    val message: String? = null,
) {
    fun countOf(f: EventFilter): Int = events.filterBy(f).size

    val visibleEvents: List<Event>
        get() = events.filterBy(filter)

    private fun List<Event>.filterBy(f: EventFilter) = when (f) {
        EventFilter.ALL -> this
        EventFilter.OPEN -> filter { it.status == EventStatus.OPEN }
        EventFilter.CLOSED -> filter { it.status == EventStatus.CLOSED }
    }
}