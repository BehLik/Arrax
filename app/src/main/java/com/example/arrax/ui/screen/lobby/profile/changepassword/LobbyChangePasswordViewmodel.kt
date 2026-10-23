package com.example.arrax.ui.screen.lobby.profile.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.Resource
import com.example.arrax.core.common.mapAuthError
import com.example.arrax.core.common.toAppError
import com.example.arrax.domain.usecase.auth.changePassword.ChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MIN_PASSWORD_LENGTH = 6

@HiltViewModel
class LobbyChangePasswordViewmodel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LobbyChangePasswordState())
    val uiState: StateFlow<LobbyChangePasswordState> = _uiState.asStateFlow()

    fun onCurrentPasswordChange(value: String) {
        _uiState.update { it.copy(currentPassword = value, currentPasswordError = null, errorGeneral = null) }
    }

    fun onNewPasswordChange(value: String) {
        _uiState.update { it.copy(newPassword = value, newPasswordError = null, errorGeneral = null) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null, errorGeneral = null) }
    }

    fun submit() {
        val s = _uiState.value
        if (s.isLoading) return

        val currentError = if (s.currentPassword.isBlank()) "Ingresa tu contraseña actual" else null
        val newError = when {
            s.newPassword.isBlank() -> "Ingresa una contraseña nueva"
            s.newPassword.length < MIN_PASSWORD_LENGTH ->
                "La contraseña debe tener al menos $MIN_PASSWORD_LENGTH caracteres"
            s.newPassword == s.currentPassword -> "La nueva contraseña debe ser distinta a la actual"
            else -> null
        }
        val confirmError = when {
            s.confirmPassword.isBlank() -> "Confirma tu contraseña"
            s.confirmPassword != s.newPassword -> "Las contraseñas no coinciden"
            else -> null
        }

        if (currentError != null || newError != null || confirmError != null) {
            _uiState.update {
                it.copy(
                    currentPasswordError = currentError,
                    newPasswordError = newError,
                    confirmPasswordError = confirmError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorGeneral = null) }

            when (val r = changePasswordUseCase(s.currentPassword, s.newPassword)) {
                is Resource.Success -> _uiState.update {
                    LobbyChangePasswordState(isSuccess = true) // limpia las contraseñas de memoria
                }
                is Resource.Error -> _uiState.update { it.withError(r) }
                Resource.Loading -> Unit
            }
        }
    }

    private fun LobbyChangePasswordState.withError(r: Resource.Error): LobbyChangePasswordState =
        when (r.throwable?.toAppError()) {
            is AppError.InvalidCredentials ->
                copy(isLoading = false, currentPasswordError = "La contraseña actual es incorrecta")
            is AppError.WeakPassword ->
                copy(isLoading = false, newPasswordError = "La contraseña es demasiado débil")
            else -> copy(
                isLoading = false,
                errorGeneral = mapAuthError(r.throwable, "No se pudo cambiar la contraseña. Intenta de nuevo.")
            )
        }
}