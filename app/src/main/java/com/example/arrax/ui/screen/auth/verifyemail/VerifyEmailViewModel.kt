package com.example.arrax.ui.screen.auth.verifyemail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.Resource
import com.example.arrax.core.common.mapAuthError
import com.example.arrax.domain.repository.AuthRepository
import com.example.arrax.domain.usecase.auth.verification.CancelRegistrationUseCase
import com.example.arrax.domain.usecase.auth.verification.CheckEmailVerifiedUseCase
import com.example.arrax.domain.usecase.auth.verification.SendEmailVerificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val sendVerification: SendEmailVerificationUseCase,
    private val checkVerified: CheckEmailVerifiedUseCase,
    private val cancelRegistration: CancelRegistrationUseCase,
    authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerifyEmailState(email = authRepository.currentEmail().orEmpty()))
    val uiState: StateFlow<VerifyEmailState> = _uiState.asStateFlow()

    fun check(onVerified: () -> Unit, silent: Boolean = false) {
        if (_uiState.value.isChecking) return
        viewModelScope.launch {
            _uiState.update { it.copy(isChecking = true, errorGeneral = null, infoMessage = null) }
            when (val r = checkVerified()) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isChecking = false) }
                    if (r.data) onVerified()
                    else if (!silent) _uiState.update { it.copy(infoMessage = "Aún no verificas tu correo") }
                }
                is Resource.Error -> _uiState.update {
                    it.copy(isChecking = false, errorGeneral = mapAuthError(r.throwable, r.message))
                }
                Resource.Loading -> Unit
            }
        }
    }

    fun resend() {
        val s = _uiState.value
        if (s.isResending || s.resendCooldown > 0) return
        viewModelScope.launch {
            _uiState.update { it.copy(isResending = true, errorGeneral = null, infoMessage = null) }
            when (val r = sendVerification()) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isResending = false, infoMessage = "Correo reenviado") }
                    startCooldown()
                }
                is Resource.Error -> _uiState.update {
                    it.copy(isResending = false, errorGeneral = mapAuthError(r.throwable, r.message))
                }
                Resource.Loading -> Unit
            }
        }
    }

    private fun startCooldown(seconds: Int = 60) {
        viewModelScope.launch {
            for (left in seconds downTo 1) {
                _uiState.update { it.copy(resendCooldown = left) }
                delay(1_000)
            }
            _uiState.update { it.copy(resendCooldown = 0) }
        }
    }

    /** Correo mal escrito: borra la cuenta sin verificar para poder registrarse de nuevo. */
    fun cancel(onDone: () -> Unit) {
        viewModelScope.launch {
            cancelRegistration()
            onDone()
        }
    }
}