package com.example.arrax.ui.screen.lobby.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.NetworkMonitor
import com.example.arrax.data.local.SesionLocalDataSource
import com.example.arrax.domain.model.Role
import com.example.arrax.domain.usecase.event.create.ObserveEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbyHomeViewmodel @Inject constructor(
    private val observeEventsUseCase: ObserveEventsUseCase,
    private val sesionLocalDataSource: SesionLocalDataSource,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _state = MutableStateFlow(LobbyHomeState())
    val state: StateFlow<LobbyHomeState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val tenantId = sesionLocalDataSource.tenantId.first() ?: return@launch
            val rol = sesionLocalDataSource.role.first()
            _state.update { it.copy(esAdmin = rol == Role.ADMIN.name) }

            combine(
                observeEventsUseCase(tenantId),
                networkMonitor.isConnected
            ) { eventos, online -> eventos to online }
                .collect { (eventos, online) ->
                    _state.update { it.copy(eventos = eventos, isLoading = false, estaSinConexion = !online) }
                }
        }
    }
}