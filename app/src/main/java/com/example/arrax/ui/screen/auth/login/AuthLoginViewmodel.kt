package com.example.arrax.ui.screen.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.domain.repository.AuthRepository
import com.example.arrax.domain.usecase.auth.login.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//@HiltViewModel
//class LoginViewModel @Inject constructor(
//    private val loginUseCase: LoginUseCase,
//    private val saveLoginStateUseCase: SaveLoginStateUseCase, // Para guardar que ya entró
//    private val authRepository: AuthRepository,
//    private val crashTracker: CrashTracker,
//    private val sessionRepository: SessionRepository
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(AuthLoginState())
//    val uiState: StateFlow<AuthLoginState> = _uiState.asStateFlow()
//    fun onEmailChange(newEmail: String) {
//        _uiState.value = _uiState.value.copy(email = newEmail)
//    }
//    fun onPasswordChange(newPassword: String) {
//        _uiState.value=_uiState.value.copy(password = newPassword)
//    }
//
//    // Función principal que se llama al presionar el botón "Iniciar Sesión"
//    fun login(onSuccess: () -> Unit) {
//        viewModelScope.launch {
//            _uiState.value=_uiState.value.copy(
//                isLoading = true,
//                errorMessage = null
//            )
//
//            val result = loginUseCase(
//                _uiState.value.email,
//                _uiState.value.password
//            )
//
//            result.fold(
//                onSuccess = { userProfile ->
//                    // Guardamos en DataStore que ya está logueado
//                    saveLoginStateUseCase(true)
//                    sessionRepository.saveUserId(userProfile.id)
//
//                    authRepository.saveUserLocally(userProfile)
//                    _uiState.value=_uiState.value.copy(isLoading = false)
//
//                    onSuccess() // Navegamos al Lobby
//                },
//                onFailure = { error ->
//                    _uiState.value=_uiState.value.copy(
//                        isLoading = false,
//                        errorMessage = error.message?:"Error desconocido"
//                    )
//
//                    crashTracker.logNonFatalException(error)
//                    crashTracker.logMessage("Login failure")
//                }
//            )
//        }
//    }
//}