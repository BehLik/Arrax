package com.example.arrax.ui.screen.lobby.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.NetworkMonitor
import com.example.arrax.domain.usecase.business.ObserveMyBusinessesUseCase
import com.example.arrax.domain.usecase.user.ObserveCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbyHomeViewmodel @Inject constructor(
    private val observeMyBusinessesUseCase: ObserveMyBusinessesUseCase,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _state = MutableStateFlow(LobbyHomeState())
    val state: StateFlow<LobbyHomeState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                observeCurrentUserUseCase(),
                observeMyBusinessesUseCase(),
                networkMonitor.isConnected
            ) { usuario, negocios, online -> Triple(usuario, negocios, online) }
                .collect { (usuario, negocios, online) ->
                    _state.update {
                        it.copy(
                            nombreUsuario = usuario?.name.orEmpty(),
                            negociosRecientes = negocios.map { b ->
                                NegocioResumen(
                                    id = b.tenant.id,
                                    nombre = b.tenant.name,
                                    esPropietario = b.esAdmin,
                                    totalEventos = b.totalEventos
                                )
                            },
                            isLoading = false,
                            estaSinConexion = !online
                        )
                    }
                }
        }
    }
}