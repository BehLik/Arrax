package com.example.arrax.ui.screen.business.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.Resource
import com.example.arrax.data.local.SessionLocalDataSource
import com.example.arrax.domain.model.Event
import com.example.arrax.domain.model.EventCut
import com.example.arrax.domain.model.EventStatus
import com.example.arrax.domain.repository.AuthRepository
import com.example.arrax.domain.usecase.catalog.GetCatalogCutUseCase
import com.example.arrax.domain.usecase.event.create.CreateEventUseCase
import com.example.arrax.domain.usecase.tenant.observe.ObserveTenantCurrentUseCase
import com.example.arrax.ui.screen.business.event.component.PriceInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
// + imports de ObserveTenantCurrentUseCase, GetCatalogCutUseCase y CreateEventUseCase (según tus paquetes)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BusinessEventCreateViewmodel @Inject constructor(
    observeTenant: ObserveTenantCurrentUseCase,
    getCatalog: GetCatalogCutUseCase,
    private val createEventUseCase: CreateEventUseCase,
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(BusinessEventCreateState())
    val state: StateFlow<BusinessEventCreateState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val role = sessionLocalDataSource.role.first()
            _state.update {
                it.copy(
                    isAdmin = role.equals("Owner", true) || role.equals("Admin", true),
                    isCheckingRole = false
                )
            }
        }

        // Mismo flujo que BusinessCatalogViewModel; `.catch` no se traga CancellationException.
        observeTenant()
            .filterNotNull()
            .map { it.id }
            .distinctUntilChanged()
            .flatMapLatest { id -> getCatalog(id).map { id to it } }
            .onEach { (id, catalog) ->
                _state.update {
                    it.copy(
                        tenantId = id,
                        cuts = catalog.filter { cut -> cut.active },
                        isLoadingCatalog = false
                    )
                }
            }
            .catch {
                _state.update { s ->
                    s.copy(isLoadingCatalog = false, errorMessage = "No se pudo cargar el catálogo")
                }
            }
            .launchIn(viewModelScope)
    }

    // Paso 1
    fun onNameChange(value: String) = _state.update { it.copy(name = value) }
    fun onDateChange(millis: Long) = _state.update { it.copy(dateMillis = millis) }
    fun onAnimalCountChange(value: Int) =
        _state.update { it.copy(animalCount = value.coerceAtLeast(1)) }

    fun onContinueFromData() = goTo(EventCreateStep.CUTS) { it.canContinueFromData }

    // Paso 2
    fun onQueryChange(q: String) = _state.update { it.copy(query = q) }

    fun onToggleCut(cutId: String) = _state.update { s ->
        val updated = s.selectedCutIds.toMutableSet()
        if (!updated.add(cutId)) updated.remove(cutId)
        s.copy(selectedCutIds = updated)
    }

    fun onContinueFromCuts() = goTo(EventCreateStep.PRICES) { it.canContinueFromCuts }

    // Paso 3
    fun onPriceChange(cutId: String, raw: String) = _state.update { s ->
        s.copy(pricesByCutId = s.pricesByCutId + (cutId to PriceInput.from(raw)))
    }

    fun onContinueFromPrices() = goTo(EventCreateStep.SUMMARY) { it.canConfirmPrices }

    // Navegación atrás
    fun onBack() = _state.update { s ->
        if (s.isSubmitting || s.step == EventCreateStep.DONE) s
        else s.copy(step = EventCreateStep.entries[(s.step.ordinal - 1).coerceAtLeast(0)])
    }

    // Paso 4 -> 5
    fun onConfirm() {
        val current = _state.value
        val tenantId = current.tenantId ?: return
        if (!current.canConfirmPrices) {
            _state.update { it.copy(errorMessage = "Todos los cortes seleccionados necesitan un precio mayor a cero") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true, errorMessage = null) }
            val uid = authRepository.observeCurrentUser().first()?.uid.orEmpty()

            val event = Event(
                name = current.name.trim(),
                date = current.dateMillis ?: 0L,
                animalCount = current.animalCount,
                status = EventStatus.OPEN,
                createdBy = uid
            )
            val cuts = current.selectedCuts.map { cut ->
                EventCut(
                    cutId = cut.id,
                    name = cut.name,
                    pricePerKgCents = current.priceCents(cut.id) ?: 0L
                )
            }

            when (val result = createEventUseCase(tenantId, event, cuts)) {
                is Resource.Success -> _state.update {
                    it.copy(isSubmitting = false, createdEventId = result.data, step = EventCreateStep.DONE)
                }
                is Resource.Error -> _state.update {
                    it.copy(isSubmitting = false, errorMessage = result.message)
                }
                Resource.Loading -> Unit
            }
        }
    }

    fun onErrorShown() = _state.update { it.copy(errorMessage = null) }

    private fun goTo(step: EventCreateStep, guard: (BusinessEventCreateState) -> Boolean) =
        _state.update { if (guard(it)) it.copy(step = step) else it }
}