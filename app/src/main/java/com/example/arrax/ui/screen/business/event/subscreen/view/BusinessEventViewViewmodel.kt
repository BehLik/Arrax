package com.example.arrax.ui.screen.business.event.subscreen.view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.arrax.core.navigation.EventList
import com.example.arrax.domain.model.Permission
import com.example.arrax.domain.model.can
import com.example.arrax.domain.repository.AuthRepository
import com.example.arrax.domain.usecase.event.create.ObserveEventsUseCase
import com.example.arrax.domain.usecase.tenant.observe.ObserveMyRoleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BusinessEventViewViewmodel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeEvents: ObserveEventsUseCase,
    observeMyRole: ObserveMyRoleUseCase,
    authRepository: AuthRepository,
) : ViewModel() {
    private val tenantId: String = checkNotNull(savedStateHandle["businessId"])
    private val _state = MutableStateFlow(BusinessEventViewState(tenantId = tenantId))
    val state = _state.asStateFlow()

    init {
        observeEvents(tenantId)
            .onEach { events ->
                _state.update {
                    it.copy(events = events.sortedByDescending { e -> e.date }, isLoading = false)
                }
            }
            .catch { _state.update { s -> s.copy(isLoading = false, message = "No se pudieron cargar los eventos") } }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            val uid = authRepository.observeCurrentUser().first()?.uid ?: return@launch
            observeMyRole(tenantId, uid)
                .map { it.can(Permission.EVENT_CREATE) }
                .catch { emit(false) }
                .collect { can -> _state.update { it.copy(canCreate = can) } }
        }
    }

    fun onFilterChange(filter: EventFilter) = _state.update { it.copy(filter = filter) }
    fun onMessageShown() = _state.update { it.copy(message = null) }
}