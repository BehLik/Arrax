package com.example.arrax.ui.screen.auth.forgotpassword


import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.Resource
import com.example.arrax.domain.usecase.auth.resetPassword.SendPasswordResetUseCase
import com.google.firebase.FirebaseNetworkException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthForGotPasswordViewmodel @Inject constructor(
    private val sendPasswordResetUseCase: SendPasswordResetUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthForGotPasswordState())
    val uiState: StateFlow<AuthForGotPasswordState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = null, errorGeneral = null) }
    }

    fun sendReset() {
        val current = _uiState.value
        val emailError = when {
            current.email.isBlank() -> "Ingresa tu correo"
            !Patterns.EMAIL_ADDRESS.matcher(current.email).matches() -> "Correo inválido"
            else -> null
        }
        if (emailError != null) {
            _uiState.update { it.copy(emailError = emailError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorGeneral = null) }

            when (val result = sendPasswordResetUseCase(current.email)) {
                is Resource.Success -> _uiState.update { it.copy(isLoading = false, isSent = true) }
                is Resource.Error -> {
                    // No se revela si el correo existe: solo un error real de red
                    // se muestra como error; cualquier otro caso (incluido
                    // "usuario no encontrado") se trata como éxito silencioso.
                    if (result.throwable is FirebaseNetworkException) {
                        _uiState.update {
                            it.copy(isLoading = false, errorGeneral = "No hay conexión a internet. Intenta de nuevo.")
                        }
                    } else {
                        _uiState.update { it.copy(isLoading = false, isSent = true) }
                    }
                }
                Resource.Loading -> Unit
            }
        }
    }
}