package com.example.arrax.ui.screen.events.home.subscreen.viewdetail

import androidx.compose.runtime.Immutable
import com.example.arrax.domain.model.CutCatalog


@Immutable
data class EventCutDetailUi(
    val cutId: String,
    val name: String,
    val pricePerKgCents: Long,
    val catalogCut: CutCatalog?
)

@Immutable
data class EventViewDetailState(
    val isLoading: Boolean = true,
    val eventName: String = "",
    val statusText: String = "",
    val isOpen: Boolean = true,
    val dateFormatted: String = "",
    val animalCount: Int = 0,
    val createdBy: String = "",
    val cuts: List<EventCutDetailUi> = emptyList(),
    val canEdit: Boolean = false
)