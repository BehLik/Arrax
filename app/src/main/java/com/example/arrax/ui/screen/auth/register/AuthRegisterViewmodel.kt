package com.example.arrax.ui.screen.auth.register


import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.Resource
import com.example.arrax.core.common.mapAuthError
import com.example.arrax.domain.usecase.auth.register.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthRegisterViewmodel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthRegisterState())
    val uiState: StateFlow<AuthRegisterState> = _uiState.asStateFlow()

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, nameError = null, errorGeneral = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = null, errorGeneral = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null, errorGeneral = null) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null, errorGeneral = null) }
    }

    /**
     * onSuccess navega al Lobby. No crea /usuarios/{uid} aquí: el documento de
     * usuario y el tenantId se resuelven al crear negocio / unirse con código.
     */
    fun register(onSuccess: () -> Unit) {
        val current = _uiState.value
        val nameError = if (current.name.isBlank()) "Ingresa tu nombre" else null
        val emailError = validateEmail(current.email)
        val passwordError = validatePassword(current.password)
        val confirmPasswordError = when {
            current.confirmPassword.isBlank() -> "Confirma tu contraseña"
            current.confirmPassword != current.password -> "Las contraseñas no coinciden"
            else -> null
        }

        if (nameError != null || emailError != null || passwordError != null || confirmPasswordError != null) {
            _uiState.update {
                it.copy(
                    nameError = nameError,
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmPasswordError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorGeneral = null) }

            when (val result = registerUseCase(current.email, current.password, current.name)) {
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

    private fun validatePassword(password: String): String? = when {
        password.isBlank() -> "Ingresa una contraseña"
        password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
        else -> null
    }
}