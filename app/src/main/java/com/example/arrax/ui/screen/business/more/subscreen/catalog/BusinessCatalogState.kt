package com.example.arrax.ui.screen.business.more.subscreen.catalog

import com.example.arrax.core.model.CutState
import com.example.arrax.domain.model.CutCatalog
import com.example.arrax.domain.model.CutOrigin

data class CutEditorState(
    val id: String? = null,              // null = corte nuevo
    val name: String = "",
    val description: String = "",
    val state: CutState = CutState.FRESH,
    val origin: CutOrigin? = null,
    val nameError: String? = null,
) {
    val isNew get() = id == null
}

data class BusinessCatalogState(
    val isLoading: Boolean = true,
    val tenantId: String? = null,
    val cuts: List<CutCatalog> = emptyList(),
    val query: String = "",
    val isGridView: Boolean = true,
    val editor: CutEditorState? = null,
    val cutToDelete: CutCatalog? = null,
    val cutToReset: CutCatalog? = null,
    val message: String? = null,
) {
    val visibleCuts: List<CutCatalog>
        get() = if (query.isBlank()) cuts
        else cuts.filter { it.name.contains(query.trim(), ignoreCase = true) }
}

data class BusinessCatalogActions(
    val onBackClick: () -> Unit = {},
    val onQueryChange: (String) -> Unit = {},
    val onToggleView: () -> Unit = {},
    val onAddClick: () -> Unit = {},
    val onEditClick: (CutCatalog) -> Unit = {},
    val onToggleActive: (CutCatalog) -> Unit = {},
    val onDeleteRequest: (CutCatalog) -> Unit = {},
    val onDeleteConfirm: () -> Unit = {},
    val onDeleteDismiss: () -> Unit = {},
    val onResetRequest: (CutCatalog) -> Unit = {},
    val onResetConfirm: () -> Unit = {},
    val onResetDismiss: () -> Unit = {},
    val onEditorChange: ((CutEditorState) -> CutEditorState) -> Unit = {},
    val onEditorSave: () -> Unit = {},
    val onEditorDismiss: () -> Unit = {},
    val onMessageShown: () -> Unit = {},
)