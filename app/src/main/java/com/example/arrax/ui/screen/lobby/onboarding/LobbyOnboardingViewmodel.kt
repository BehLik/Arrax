package com.example.arrax.ui.screen.lobby.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.Resource
import com.example.arrax.data.local.SesionLocalDataSource
import com.example.arrax.domain.model.Role
import com.example.arrax.domain.repository.AuthRepository
import com.example.arrax.domain.usecase.event.tenant.CreateTenantUseCase
import com.example.arrax.domain.usecase.event.tenant.JoinTenantUseCase
import com.example.arrax.domain.usecase.event.tenant.ObserveTenantCurrentUseCase
import com.example.arrax.domain.usecase.user.ObserveCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbyOnboardingViewmodel @Inject constructor(
    private val authRepository: AuthRepository,
    private val observeTenantCurrentUseCase: ObserveTenantCurrentUseCase,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val createTenantUseCase: CreateTenantUseCase,
    private val joinTenantUseCase: JoinTenantUseCase,
    private val sesionLocalDataSource: SesionLocalDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(LobbyOnboardingState())
    val state: StateFlow<LobbyOnboardingState> = _state.asStateFlow()

    private val _onboardingCompletado = MutableSharedFlow<Unit>()
    val onboardingCompletado: SharedFlow<Unit> = _onboardingCompletado.asSharedFlow()

    init {
        viewModelScope.launch {
            val tenant = observeTenantCurrentUseCase().first()
            if (tenant != null) {
                val rol = observeCurrentUserUseCase().first()?.role ?: Role.OPERADOR
                sesionLocalDataSource.saveSession(tenant.id, rol.name)
                _onboardingCompletado.emit(Unit)
            } else {
                _state.update { it.copy(isResolviendoTenant = false) }
            }
        }
    }

    fun onModoSeleccionado(modo: OnboardingModo) { _state.update { it.copy(modo = modo, errorGeneral = null) } }

    fun onVolverASeleccion() {
        _state.update {
            it.copy(modo = OnboardingModo.SELECCION, nombreNegocioError = null, codigoInvitacionError = null, errorGeneral = null)
        }
    }

    fun onNombreNegocioChanged(value: String) { _state.update { it.copy(nombreNegocio = value, nombreNegocioError = null) } }

    fun onCodigoInvitacionChanged(value: String) { _state.update { it.copy(codigoInvitacion = value.trim(), codigoInvitacionError = null) } }

    fun onCrearNegocio() {
        val nombre = _state.value.nombreNegocio.trim()
        if (nombre.isBlank()) {
            _state.update { it.copy(nombreNegocioError = "Ingresa el nombre de tu negocio") }
            return
        }
        if (nombre.length < 3) {
            _state.update { it.copy(nombreNegocioError = "El nombre debe tener al menos 3 caracteres") }
            return
        }

        viewModelScope.launch {
            val uid = authRepository.observeCurrentUser().first()?.uid ?: return@launch
            _state.update { it.copy(isLoading = true, errorGeneral = null) }
            when (val resultado = createTenantUseCase(nombre, uid)) {
                is Resource.Success -> onTenantResuelto(resultado.data.id, Role.ADMIN)
                is Resource.Error -> _state.update { it.copy(isLoading = false, errorGeneral = resultado.message) }
                is Resource.Loading -> Unit
            }
        }
    }

    fun onUnirseNegocio() {
        val codigo = _state.value.codigoInvitacion.trim()
        if (codigo.isBlank()) {
            _state.update { it.copy(codigoInvitacionError = "Ingresa el código de invitación") }
            return
        }

        viewModelScope.launch {
            val uid = authRepository.observeCurrentUser().first()?.uid ?: return@launch
            _state.update { it.copy(isLoading = true, errorGeneral = null) }
            when (val resultado = joinTenantUseCase(codigo, uid)) {
                is Resource.Success -> {
                    // el rol lo asigna la invitación; se lee de /usuarios/{uid} recién
                    // escrito por JoinTenantUseCase, no viaja en el Resource<Tenant>
                    val rol = observeCurrentUserUseCase().first()?.role ?: Role.OPERADOR
                    onTenantResuelto(resultado.data.id, rol)
                }
                is Resource.Error -> _state.update { it.copy(isLoading = false, codigoInvitacionError = resultado.message) }
                is Resource.Loading -> Unit
            }
        }
    }

    private suspend fun onTenantResuelto(tenantId: String, rol: Role) {
        sesionLocalDataSource.saveSession(tenantId, rol.name)
        _state.update { it.copy(isLoading = false) }
        _onboardingCompletado.emit(Unit)
    }
}