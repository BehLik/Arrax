package com.example.arrax.ui.screen.lobby.join

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.NetworkMonitor
import com.example.arrax.core.common.Resource
import com.example.arrax.core.common.toAppError
import com.example.arrax.core.model.JoinMethod
import com.example.arrax.domain.usecase.tenant.JoinTenantUseCase
import com.example.arrax.domain.usecase.user.ObserveCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbyJoinViewModel @Inject constructor(
    private val joinTenant: JoinTenantUseCase,
    private val observeCurrentUser: ObserveCurrentUserUseCase,
    private val networkMonitor: NetworkMonitor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LobbyJoinState())
    val uiState: StateFlow<LobbyJoinState> = _uiState.asStateFlow()

    private val _events = Channel<LobbyJoinEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var lastQr: String? = null
    private var lastQrAt = 0L
    private companion object { const val SUCCESS_DELAY_MS = 800L }
    fun onMethodChange(method: JoinMethod) {
        _uiState.update { it.copy(method = method, errorMessage = null) }
    }

    fun onCodeChange(raw: String) {
        _uiState.update { it.copy(code = raw.uppercase(), errorMessage = null) }
    }

    fun onTorchToggle() {
        _uiState.update { it.copy(torchOn = !it.torchOn) }
    }

    /** Para cuando conectes CameraX: recibe el texto crudo del QR (deep link o código). */
    fun onQrScanned(raw: String) {
        val now = System.currentTimeMillis()
        if (raw == lastQr && now - lastQrAt < 3_000) return
        lastQr = raw
        lastQrAt = now
        join(extractInviteCode(raw))
    }

    private fun extractInviteCode(raw: String): String {
        val text = raw.trim()
        return runCatching { Uri.parse(text).getQueryParameter("code") }
            .getOrNull()
            ?.takeIf { it.isNotBlank() }
            ?: text.substringAfterLast('/')   // si el QR trae solo el código
    }

    fun onJoin() = join(_uiState.value.code)

    private fun join(rawCode: String) {
        if (_uiState.value.isLoading) return
        // El código se muestra con formato (display), pero en Firestore el id va limpio.
        val code = rawCode.filter { it.isLetterOrDigit() }.uppercase()

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Las escrituras de Firestore no terminan sin red: evita dejar el botón colgado
            if (!networkMonitor.isConnected.first()) {
                fail("Necesitas conexión para unirte a un negocio.")
                return@launch
            }

            val user = observeCurrentUser().first()
            if (user == null) {
                fail("No se encontró tu sesión. Inicia sesión de nuevo.")
                return@launch
            }

            when (val result = joinTenant(code, user.uid)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isSuccess = true) }   // isLoading sigue en true
                    delay(SUCCESS_DELAY_MS)
                    _events.send(LobbyJoinEvent.Joined(result.data.id))
                }
                is Resource.Error -> fail(
                    when (result.throwable?.toAppError()) {
                        is AppError.InviteExpired -> "Esta invitación expiró o fue revocada."
                        is AppError.AlreadyMember -> "Ya eres miembro de este negocio."
                        is AppError.InviteNotFound -> "Código de invitación inválido."
                        is AppError.PermissionDenied -> "No tienes permiso para unirte con este código."
                        is AppError.Network -> "No hay conexión a internet. Intenta de nuevo."
                        else -> "No se pudo unir al negocio. Intenta de nuevo."
                    }
                )
                Resource.Loading -> Unit
            }
        }
    }

    private fun fail(message: String) {
        _uiState.update { it.copy(isLoading = false, errorMessage = message) }
    }
}