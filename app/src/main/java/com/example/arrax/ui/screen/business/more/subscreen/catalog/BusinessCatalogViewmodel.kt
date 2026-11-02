package com.example.arrax.ui.screen.business.more.subscreen.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.domain.model.CutCatalog
import com.example.arrax.domain.usecase.catalog.DeleteCatalogCutUseCase
import com.example.arrax.domain.usecase.catalog.GetCatalogCutUseCase
import com.example.arrax.domain.usecase.catalog.ResetCatalogCutUseCase
import com.example.arrax.domain.usecase.catalog.SaveCatalogCutUseCase
import com.example.arrax.domain.usecase.tenant.observe.ObserveTenantCurrentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class BusinessCatalogViewModel @Inject constructor(
    observeTenant: ObserveTenantCurrentUseCase,
    private val getCatalog: GetCatalogCutUseCase,
    private val saveCut: SaveCatalogCutUseCase,
    private val deleteCut: DeleteCatalogCutUseCase,
    private val resetCut: ResetCatalogCutUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(BusinessCatalogState())
    val state = _state.asStateFlow()

    init {
        observeTenant()
            .filterNotNull()
            .map { it.id }
            .distinctUntilChanged()
            .flatMapLatest { id -> getCatalog(id).map { id to it } }
            .onEach { (id, cuts) -> _state.update { it.copy(tenantId = id, cuts = cuts, isLoading = false) } }
            .catch { _state.update { s -> s.copy(isLoading = false, message = "No se pudo cargar el catálogo") } }
            .launchIn(viewModelScope)
    }

    fun onQueryChange(q: String) = _state.update { it.copy(query = q) }
    fun onToggleView() = _state.update { it.copy(isGridView = !it.isGridView) }
    fun onMessageShown() = _state.update { it.copy(message = null) }

    // Editor
    fun onAddClick() = _state.update { it.copy(editor = CutEditorState()) }

    fun onEditClick(cut: CutCatalog) = _state.update {
        it.copy(
            editor = CutEditorState(
                id = cut.id, name = cut.name, description = cut.description.orEmpty(),
                state = cut.state, origin = cut.origin,
            )
        )
    }

    fun onEditorChange(transform: (CutEditorState) -> CutEditorState) =
        _state.update { s -> s.copy(editor = s.editor?.let(transform)) }

    fun onEditorDismiss() = _state.update { it.copy(editor = null) }

    fun onEditorSave() {
        val s = _state.value
        val editor = s.editor ?: return
        val tenantId = s.tenantId ?: return
        val name = editor.name.trim()
        val error = when {
            name.isEmpty() -> "Escribe un nombre"
            s.cuts.any { it.id != editor.id && it.name.equals(name, ignoreCase = true) } ->
                "Ya existe un corte con ese nombre"
            else -> null
        }
        if (error != null) {
            _state.update { it.copy(editor = editor.copy(nameError = error)) }
            return
        }
        val base = s.cuts.firstOrNull { it.id == editor.id } ?: CutCatalog()
        val cut = base.copy(name = name, description = editor.description, state = editor.state)
        _state.update { it.copy(editor = null) }
        viewModelScope.launch {
            saveCut(tenantId, cut).onFailure { fail("No se pudo guardar el corte") }
        }
    }

    // Ocultar / mostrar
    fun onToggleActive(cut: CutCatalog) {
        val tenantId = _state.value.tenantId ?: return
        viewModelScope.launch {
            saveCut(tenantId, cut.copy(active = !cut.active)).onFailure { fail("No se pudo actualizar") }
        }
    }

    // Eliminar (solo personalizados)
    fun onDeleteRequest(cut: CutCatalog) = _state.update { it.copy(cutToDelete = cut) }
    fun onDeleteDismiss() = _state.update { it.copy(cutToDelete = null) }
    fun onDeleteConfirm() {
        val s = _state.value
        val cut = s.cutToDelete ?: return
        val tenantId = s.tenantId ?: return
        _state.update { it.copy(cutToDelete = null) }
        viewModelScope.launch { deleteCut(tenantId, cut).onFailure { fail("No se pudo eliminar") } }
    }

    // Restablecer al default
    fun onResetRequest(cut: CutCatalog) = _state.update { it.copy(cutToReset = cut) }
    fun onResetDismiss() = _state.update { it.copy(cutToReset = null) }
    fun onResetConfirm() {
        val s = _state.value
        val cut = s.cutToReset ?: return
        val tenantId = s.tenantId ?: return
        _state.update { it.copy(cutToReset = null) }
        viewModelScope.launch { resetCut(tenantId, cut.id).onFailure { fail("No se pudo restablecer") } }
    }

    private fun fail(msg: String) = _state.update { it.copy(message = msg) }
}