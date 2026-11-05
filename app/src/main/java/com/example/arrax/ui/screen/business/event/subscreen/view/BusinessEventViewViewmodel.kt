package com.example.arrax.ui.screen.business.event.subscreen.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.domain.usecase.event.create.ObserveEventsUseCase
import com.example.arrax.domain.usecase.tenant.observe.ObserveTenantCurrentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BusinessEventViewViewmodel @Inject constructor(
    observeTenant: ObserveTenantCurrentUseCase,
    observeEvents: ObserveEventsUseCase, // ASUMIDO
) : ViewModel() {

    private val _state = MutableStateFlow(BusinessEventViewState())
    val state = _state.asStateFlow()

    init {
        observeTenant()
            .filterNotNull()
            .map { it.id }
            .distinctUntilChanged()
            .flatMapLatest { id -> observeEvents(id).map { id to it } }
            .onEach { (id, events) ->
                _state.update {
                    it.copy(tenantId = id, events = events.sortedByDescending { e -> e.date }, isLoading = false)
                }
            }
            .catch { _state.update { s -> s.copy(isLoading = false, message = "No se pudieron cargar los eventos") } }
            .launchIn(viewModelScope)
    }

    fun onFilterChange(filter: EventFilter) = _state.update { it.copy(filter = filter) }
    fun onMessageShown() = _state.update { it.copy(message = null) }
}