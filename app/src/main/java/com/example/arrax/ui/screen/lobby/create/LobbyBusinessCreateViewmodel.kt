package com.example.arrax.ui.screen.lobby.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.NetworkMonitor
import com.example.arrax.core.common.toAppError
import com.example.arrax.domain.usecase.business.create.CreateBusinessUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbyBusinessCreateViewmodel @Inject constructor(
    private val createBusiness: CreateBusinessUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _state = MutableStateFlow(LobbyBusinessCreateState())
    val state: StateFlow<LobbyBusinessCreateState> = _state.asStateFlow()

    private val _created = Channel<String>(Channel.BUFFERED)
    val created = _created.receiveAsFlow() // emite el businessId

    fun onNameChange(value: String) {
        _state.update {
            it.copy(name = value.take(CreateBusinessUseCase.MAX_NAME), nameError = null, error = null)
        }
    }

    fun onSubmit() {
        val current = _state.value
        if (!current.canSubmit) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, nameError = null, error = null) }
            // batch.commit().await() no termina sin red: se evita dejar el botón colgado
            if (!networkMonitor.isConnected.first()) {
                _state.update { it.copy(isLoading = false, error = "Necesitas conexión para crear un negocio.") }
                return@launch
            }
            createBusiness(current.name)
                .onSuccess { tenant ->
                    _state.update { it.copy(isLoading = false) }
                    _created.send(tenant.id)
                }
                .onFailure { e ->
                    val appError = e.toAppError()
                    _state.update {
                        if (appError is AppError.Validation) {
                            it.copy(isLoading = false, nameError = "Escribe un nombre para tu negocio.")
                        } else {
                            it.copy(isLoading = false, error = appError.toMessage())
                        }
                    }
                }
        }
    }

    private fun AppError.toMessage() = when (this) {
        is AppError.Network -> "No hay conexión a internet. Intenta de nuevo."
        is AppError.PermissionDenied -> "No tienes permiso para crear el negocio."
        else -> "No se pudo crear el negocio. Intenta de nuevo."
    }
}