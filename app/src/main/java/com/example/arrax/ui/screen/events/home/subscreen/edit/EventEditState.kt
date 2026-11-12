package com.example.arrax.ui.screen.events.home.subscreen.edit

import androidx.compose.runtime.Immutable
import com.example.arrax.domain.model.CutCatalog


enum class EventEditStep { DATA, CUTS, PICK }

@Immutable
data class EditableCut(val cutId: String, val name: String, val priceText: String)

@Immutable
data class EventEditState(
    val isLoading: Boolean = true,
    val canEdit: Boolean? = null,              // null = aún no se sabe
    val isSaving: Boolean = false,
    val step: EventEditStep = EventEditStep.DATA,
    val name: String = "",
    val dateMillis: Long = 0L,
    val animalCount: Int = 1,
    val cuts: List<EditableCut> = emptyList(),
    val catalog: List<CutCatalog> = emptyList(),
    val isLoadingCatalog: Boolean = true,
    val query: String = "",
    val errorMessage: String? = null,
    val isSaved: Boolean = false
) {
    val selectedIds: Set<String> get() = cuts.mapTo(HashSet()) { it.cutId }
    val visibleCatalog: List<CutCatalog>
        get() = if (query.isBlank()) catalog
        else catalog.filter { it.name.contains(query.trim(), ignoreCase = true) }
    val canContinueFromData: Boolean
        get() = name.isNotBlank() && animalCount > 0
}