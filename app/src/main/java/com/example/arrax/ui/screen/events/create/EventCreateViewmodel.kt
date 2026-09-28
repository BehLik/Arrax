package com.example.arrax.ui.screen.events.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.Resource
import com.example.arrax.data.local.SesionLocalDataSource
import com.example.arrax.domain.model.Event
import com.example.arrax.domain.model.EventCut
import com.example.arrax.domain.model.EventStatus
import com.example.arrax.domain.repository.AuthRepository
import com.example.arrax.domain.usecase.event.create.CreateEventUseCase
import com.example.arrax.domain.usecase.event.create.GetCatalogCutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventCreateViewmodel @Inject constructor(
    private val createEventUseCase: CreateEventUseCase,
    private val getCatalogCutUseCase: GetCatalogCutUseCase,
    private val sessionLocalDataSource: SesionLocalDataSource,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EventCreateState())
    val state: StateFlow<EventCreateState> = _state

    init {
        viewModelScope.launch {
            val role = sessionLocalDataSource.role.first()
            _state.update {
                it.copy(isAdmin = role.equals("ADMIN", ignoreCase = true), isCheckingRole = false)
            }
            loadCatalog()
        }
    }

    // FIX: GetCatalogCutUseCase devuelve Flow<List<CutCatalog>> directo (via
    // CutCatalogRepository.observeCatalog), no Resource. Se colecta el flow en
    // vez de hacer un "when" sobre Resource.Success/Error.
    private fun loadCatalog() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingCatalog = true) }
            val tenantId = sessionLocalDataSource.tenantId.first().orEmpty()
            try {
                getCatalogCutUseCase(tenantId).collect { catalog ->
                    _state.update {
                        it.copy(availableCuts = catalog.filter { cut -> cut.active }, isLoadingCatalog = false)
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = e.message, isLoadingCatalog = false) }
            }
        }
    }

    // Paso 1 - datos basicos
    fun onNameChange(value: String) = _state.update { it.copy(name = value) }
    fun onDateChange(millis: Long) = _state.update { it.copy(dateMillis = millis) }
    fun onPigCountChange(value: Int) = _state.update { it.copy(pigCount = value.coerceAtLeast(1)) }

    fun onContinueFromBasicData() {
        if (_state.value.canContinueFromBasicData) {
            _state.update { it.copy(step = 1) }
        }
    }

    // Paso 2 - seleccion de cortes
    fun onToggleCut(cutId: String) {
        _state.update { current ->
            val updated = current.selectedCutIds.toMutableSet()
            if (!updated.add(cutId)) updated.remove(cutId)
            current.copy(selectedCutIds = updated)
        }
    }

    fun onContinueFromCuts() {
        if (_state.value.canContinueFromCuts) {
            _state.update { it.copy(step = 2) }
        }
    }

    // Paso 3 - precios
    fun onPriceChange(cutId: String, rawValue: String) {
        _state.update { current ->
            current.copy(pricesByCutId = current.pricesByCutId + (cutId to rawValue))
        }
    }

    fun onBack() {
        _state.update { it.copy(step = (it.step - 1).coerceAtLeast(0)) }
    }

    fun onConfirm() {
        val current = _state.value
        if (!current.canConfirm) {
            _state.update { it.copy(errorMessage = "Todos los cortes seleccionados necesitan un precio mayor a cero") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true, errorMessage = null) }

            val tenantId = sessionLocalDataSource.tenantId.first().orEmpty()
            val uid = authRepository.observeCurrentUser().first()?.uid.orEmpty()

            val event = Event(
                name = current.name,
                date = current.dateMillis ?: 0L,
                pigCount = current.pigCount,
                status = EventStatus.OPEN,
                createdBy = uid
            )

            val cuts = current.selectedCuts.map { cut ->
                EventCut(
                    cutId = cut.id,
                    name = cut.name,
                    pricePerKg = current.pricesByCutId[cut.id]?.toDoubleOrNull() ?: 0.0
                )
            }

            when (val result = createEventUseCase(tenantId, event, cuts)) {
                is Resource.Success -> _state.update {
                    it.copy(isSubmitting = false, createdEventId = result.data)
                }
                is Resource.Error -> _state.update {
                    it.copy(isSubmitting = false, errorMessage = result.message)
                }
                Resource.Loading -> Unit
            }
        }
    }

    fun onErrorShown() = _state.update { it.copy(errorMessage = null) }
}
