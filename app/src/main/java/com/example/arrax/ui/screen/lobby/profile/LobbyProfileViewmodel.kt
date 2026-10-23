package com.example.arrax.ui.screen.lobby.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.domain.usecase.auth.login.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class LobbyProfileViewmodel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LobbyProfileState())
    val uiState: StateFlow<LobbyProfileState> = _uiState.asStateFlow()

    /** onLoggedOut navega a Auth; solo se llama cuando la sesión ya se cerró. */
    fun logout(onLoggedOut: () -> Unit) {
        if (_uiState.value.isLoggingOut) return
        _uiState.update { it.copy(isLoggingOut = true) }

        viewModelScope.launch {
            try {
                logoutUseCase()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("LobbyProfile", "No se pudo cerrar sesión", e)
                _uiState.update { it.copy(isLoggingOut = false) }
                return@launch
            }
            onLoggedOut()
        }
    }
}