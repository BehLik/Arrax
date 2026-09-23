package com.example.arrax.ui.screen.auth.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.Resource
import com.example.arrax.core.common.mapAuthError
import com.example.arrax.domain.usecase.auth.login.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthLoginViewmodel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthLoginState())
    val uiState: StateFlow<AuthLoginState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = null, errorGeneral = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null, errorGeneral = null) }
    }

    /** onSuccess navega a un punto de decisión (Fase 2), no directo a Lobby Home. */
    fun login(onSuccess: () -> Unit) {
        val current = _uiState.value
        val emailError = validateEmail(current.email)
        val passwordError = validatePassword(current.password)

        if (emailError != null || passwordError != null) {
            _uiState.update { it.copy(emailError = emailError, passwordError = passwordError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorGeneral = null) }

            when (val result = loginUseCase(current.email, current.password)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                is Resource.Error -> _uiState.update {
                    it.copy(isLoading = false, errorGeneral = mapAuthError(result.throwable, result.message))
                }
                Resource.Loading -> Unit
            }
        }
    }

    private fun validateEmail(email: String): String? = when {
        email.isBlank() -> "Ingresa tu correo"
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Correo inválido"
        else -> null
    }

    private fun validatePassword(password: String): String? =
        if (password.isBlank()) "Ingresa tu contraseña" else null
}
