package com.example.arrax.ui.screen.events.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.Resource
import androidx.navigation.toRoute
import com.example.arrax.core.navigation.EventDetail
import com.example.arrax.data.local.SessionLocalDataSource
import com.example.arrax.domain.usecase.event.detail.CloseEventUseCase
import com.example.arrax.domain.usecase.event.detail.ObserveEventDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeEventDetailUseCase: ObserveEventDetailUseCase,
    private val closeEventUseCase: CloseEventUseCase,
    private val sessionLocalDataSource: SessionLocalDataSource
) : ViewModel() {

    private val route = savedStateHandle.toRoute<EventDetail>()
    private val tenantId: String = route.businessId
    private val eventId: String = route.eventId

    private val _state = MutableStateFlow(EventDetailState())
    val state: StateFlow<EventDetailState> = _state

    init {
        viewModelScope.launch {
            val role = sessionLocalDataSource.role.first()
            _state.update { it.copy(isAdmin = role.equals("ADMIN", ignoreCase = true)) }

            observeEventDetailUseCase(tenantId, eventId)
                .onEach { eventWithCuts ->
                    _state.update { it.copy(isLoading = false, eventWithCuts = eventWithCuts) }
                }
                .launchIn(viewModelScope)
        }
    }

    fun onCloseEventClick() = _state.update { it.copy(showCloseConfirmation = true) }
    fun onDismissCloseConfirmation() = _state.update { it.copy(showCloseConfirmation = false) }

    fun onConfirmCloseEvent() {
        viewModelScope.launch {
            _state.update { it.copy(isClosing = true, showCloseConfirmation = false) }
            when (val result = closeEventUseCase(tenantId, eventId)) {
                is Resource.Success -> _state.update { it.copy(isClosing = false) }
                is Resource.Error -> _state.update { it.copy(isClosing = false, errorMessage = result.message) }
                Resource.Loading -> Unit
            }
        }
    }

    fun onErrorShown() = _state.update { it.copy(errorMessage = null) }
}
