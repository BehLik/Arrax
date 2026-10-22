package com.example.arrax.ui.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.navigation.AuthGraph
import com.example.arrax.core.navigation.LobbyGraph
import com.example.arrax.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
data class StartState(val root: Any, val needsVerification: Boolean = false)
@HiltViewModel
class AppStartViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _start = MutableStateFlow<StartState?>(null)
    val start: StateFlow<StartState?> = _start.asStateFlow()

    init {
        viewModelScope.launch {
            val user = authRepository.observeCurrentUser().first()
            _start.value = when {
                user == null -> StartState(AuthGraph)
                !authRepository.isEmailVerified() -> StartState(AuthGraph, needsVerification = true)
                else -> StartState(LobbyGraph)
            }
        }
    }
}
