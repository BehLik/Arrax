package com.example.arrax.ui.screen.events.home.subscreen.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.arrax.core.common.Resource
import com.example.arrax.core.navigation.EventEdit
import com.example.arrax.domain.model.Access
import com.example.arrax.domain.model.EventCut
import com.example.arrax.domain.model.Permission
import com.example.arrax.domain.usecase.access.ObserveAccessUseCase
import com.example.arrax.domain.usecase.catalog.GetCatalogCutUseCase
import com.example.arrax.domain.usecase.event.detail.ObserveEventDetailUseCase
import com.example.arrax.domain.usecase.event.edit.UpdateEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.math.RoundingMode
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


@HiltViewModel
class EventEditViewmodel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeEventDetail: ObserveEventDetailUseCase,
    observeCatalog: GetCatalogCutUseCase,
//    observeCatalog: ObserveCatalogUseCase,
    observeAccess: ObserveAccessUseCase,
    private val updateEvent: UpdateEventUseCase
) : ViewModel() {

    private val route = savedStateHandle.toRoute<EventEdit>()
    private val _state = MutableStateFlow(EventEditState())
    val state: StateFlow<EventEditState> = _state.asStateFlow()

    private var originalCutIds: Set<String> = emptySet()

    init {
        // Carga única: no se sobreescriben las ediciones con snapshots posteriores.
        viewModelScope.launch {
            try {
                val detail = observeEventDetail(route.businessId, route.eventId)
                    .filterNotNull().first()
                originalCutIds = detail.cuts.mapTo(HashSet()) { it.cutId }
                _state.update {
                    it.copy(
                        isLoading = false,
                        name = detail.event.name,
                        dateMillis = detail.event.date,
                        animalCount = detail.event.animalCount,
                        cuts = detail.cuts.map { c ->
                            EditableCut(c.cutId, c.name, centsToText(c.pricePerKgCents))
                        }
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, errorMessage = "No se pudo cargar el evento") }
            }
        }

        observeCatalog(route.businessId)
            .onEach { c -> _state.update { it.copy(catalog = c, isLoadingCatalog = false) } }
            .catch { _state.update { s -> s.copy(isLoadingCatalog = false) } }
            .launchIn(viewModelScope)

        observeAccess(route.businessId)
            .onEach { a ->
                val can = when (a) {
                    Access.Loading -> null
                    Access.None -> false
                    is Access.Granted -> a.can(Permission.EVENT_EDIT)
                }
                _state.update { it.copy(canEdit = can) }
            }
            .launchIn(viewModelScope)
    }

    fun onNameChange(v: String) = _state.update { it.copy(name = v) }
    fun onDateChange(v: Long) = _state.update { it.copy(dateMillis = v) }
    fun onAnimalCountChange(value: Int) =
        _state.update { it.copy(animalCount = value.coerceAtLeast(1)) }

    fun onQueryChange(v: String) = _state.update { it.copy(query = v) }

    fun onPriceChange(cutId: String, text: String) = _state.update { s ->
        s.copy(cuts = s.cuts.map { if (it.cutId == cutId) it.copy(priceText = text) else it })
    }

    fun onRemoveCut(cutId: String) =
        _state.update { s -> s.copy(cuts = s.cuts.filterNot { it.cutId == cutId }) }

    fun onToggleCut(cutId: String) = _state.update { s ->
        if (cutId in s.selectedIds) {
            s.copy(cuts = s.cuts.filterNot { it.cutId == cutId })
        } else {
            val cut = s.catalog.firstOrNull { it.id == cutId } ?: return@update s
            s.copy(cuts = s.cuts + EditableCut(cut.id, cut.name, priceText = ""))
        }
    }

    fun onContinueFromData() = _state.update { it.copy(step = EventEditStep.CUTS) }
    fun onOpenPicker() = _state.update { it.copy(step = EventEditStep.PICK) }
    fun onClosePicker() = _state.update { it.copy(step = EventEditStep.CUTS, query = "") }

    fun onBack() = _state.update {
        when (it.step) {
            EventEditStep.PICK -> it.copy(step = EventEditStep.CUTS, query = "")
            EventEditStep.CUTS -> it.copy(step = EventEditStep.DATA)
            EventEditStep.DATA -> it
        }
    }

    fun onErrorShown() = _state.update { it.copy(errorMessage = null) }

    fun onSave() {
        val s = _state.value
        if (s.isSaving) return
        val cuts = s.cuts.map { c ->
            val cents = textToCents(c.priceText)
            if (cents == null || cents <= 0L) {
                _state.update { it.copy(errorMessage = "Falta el precio de ${c.name}") }
                return
            }
            EventCut(cutId = c.cutId, name = c.name, pricePerKgCents = cents)
        }
        val removed = (originalCutIds - cuts.map { it.cutId }.toSet()).toList()

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            when (val r = updateEvent(
                tenantId = route.businessId,
                eventId = route.eventId,
                name = s.name,
                date = s.dateMillis,
                animalCount = s.animalCount,
                cuts = cuts,
                removedCutIds = removed
            )) {
                is Resource.Success -> _state.update { it.copy(isSaving = false, isSaved = true) }
                is Resource.Error -> _state.update { it.copy(isSaving = false, errorMessage = r.message) }
                Resource.Loading -> Unit
            }
        }
    }

    private fun centsToText(cents: Long) = "%d.%02d".format(cents / 100, cents % 100)

    private fun textToCents(text: String): Long? =
        text.trim().replace(',', '.').toBigDecimalOrNull()
            ?.movePointRight(2)?.setScale(0, RoundingMode.HALF_UP)?.toLong()
}