package com.example.arrax.ui.screen.events.orders.subscreen.make

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.EventCut
import com.example.arrax.domain.model.EventStatus
import com.example.arrax.domain.model.OrderLineInput
import com.example.arrax.domain.model.RequestMode
import com.example.arrax.domain.usecase.catalog.GetCatalogCutUseCase
import com.example.arrax.domain.usecase.customer.Observe.ObserveCustomersUseCase
import com.example.arrax.domain.usecase.event.detail.ObserveEventDetailUseCase
import com.example.arrax.domain.usecase.order.create.CreateOrderUseCase
import com.example.arrax.ui.screen.events.orders.subscreen.make.component.toCatalogImage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EventMakeOrderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeCustomers: ObserveCustomersUseCase,
    observeEventDetail: ObserveEventDetailUseCase,
    getCatalog: GetCatalogCutUseCase,
    private val createOrder: CreateOrderUseCase,
) : ViewModel() {

    // El businessId de la ruta es el tenantId
    private val tenantId: String = checkNotNull(savedStateHandle.get<String>("businessId"))
    private val eventId: String = checkNotNull(savedStateHandle.get<String>("eventId"))

    private val _state = MutableStateFlow(EventMakeOrderState())
    val state: StateFlow<EventMakeOrderState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeCustomers(tenantId)
                .catch { e -> _state.update { it.copy(errorMessage = e.message) } }
                .collect { list -> _state.update { it.copy(customers = list) } }
        }
        viewModelScope.launch {
            observeEventDetail(tenantId, eventId)
                .catch { e -> _state.update { it.copy(isLoading = false, errorMessage = e.message) } }
                .collect { data ->
                    val cuts = data?.cuts.orEmpty()
                    _state.update { s ->
                        s.copy(
                            isLoading = false,
                            eventOpen = data?.event?.status == EventStatus.OPEN,
                            eventCuts = cuts,
                            // Si cambió un precio, se refresca; si quitaron el corte, se descarta la línea
                            lines = s.lines.mapNotNull { line ->
                                cuts.firstOrNull { it.cutId == line.cutId }?.let {
                                    line.copy(pricePerKgCents = it.pricePerKgCents.toLong())
                                }
                            },
                        )
                    }
                }
        }
        viewModelScope.launch {
            getCatalog(tenantId)
                .catch { /* sin imagen no es crítico */ }
                .collect { list ->
                    _state.update { it.copy(cutImages = list.associate { c -> c.id to c.toCatalogImage() }) }
                }
        }
//        viewModelScope.launch {
//            savedStateHandle.getStateFlow<String?>(SELECTED_CUSTOMER_ID_KEY, null)
//                .filterNotNull()
//                .collect { id ->
//                    _state.update { it.copy(selectedCustomerId = id) }
//                    savedStateHandle[SELECTED_CUSTOMER_ID_KEY] = null
//                }
//        }

    }

    fun onCustomerSelected(customerId: String) =
        _state.update { it.copy(selectedCustomerId = customerId) }

    fun onAddCutClick() = _state.update { it.copy(showCutPicker = true) }
    fun onDismissCutPicker() = _state.update { it.copy(showCutPicker = false) }

    fun onCutPicked(cut: EventCut) = _state.update { s ->
        if (s.lines.any { it.cutId == cut.cutId }) s.copy(showCutPicker = false)
        else s.copy(
            showCutPicker = false,
            lines = s.lines + OrderLineUi(
                cutId = cut.cutId,
                cutName = cut.name,
                pricePerKgCents = cut.pricePerKgCents.toLong(),
                grams = 1000,
            ),
        )
    }

    fun onQuantityChange(cutId: String, kg: Double) {
        val grams = (kg * 1000).roundToInt().coerceAtLeast(0)
        _state.update { s ->
            s.copy(lines = s.lines.map { if (it.cutId == cutId) it.copy(grams = grams) else it })
        }
    }

    fun onRemoveLine(cutId: String) =
        _state.update { s -> s.copy(lines = s.lines.filterNot { it.cutId == cutId }) }

    fun onNotesChange(value: String) = _state.update { it.copy(notes = value) }
    fun onErrorShown() = _state.update { it.copy(errorMessage = null) }


    fun onConfirm() {
        val s = _state.value
        val customerId = s.selectedCustomerId ?: return
        if (!s.canConfirm) return
        _state.update { it.copy(isSaving = true, errorMessage = null) }
        viewModelScope.launch {
            val result = createOrder(
                tenantId = tenantId,
                eventId = eventId,
                customerId = customerId,
                notes = s.notes,
                lines = s.lines.map {
                    OrderLineInput(
                        cutId = it.cutId,
                        requestedBy = RequestMode.KG,
                        requestedGrams = it.grams,
                    )
                },
            )
            when (result) {
                is Resource.Success -> _state.update { it.copy(isSaving = false, saved = true) }
                is Resource.Error -> _state.update { it.copy(isSaving = false, errorMessage = result.message) }
                else -> _state.update { it.copy(isSaving = false) }
            }
        }
    }
}