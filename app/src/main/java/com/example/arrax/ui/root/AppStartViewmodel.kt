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

@HiltViewModel
class AppStartViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // null = still resolving. Type-safe routes are plain @Serializable objects, hence Any.
    private val _startDestination = MutableStateFlow<Any?>(null)
    val startDestination: StateFlow<Any?> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val user = authRepository.observeCurrentUser().first()
            // Root NavHost start must be a graph (AuthGraph/LobbyGraph), not a nested destination.
            _startDestination.value = if (user == null) AuthGraph else LobbyGraph
        }
    }
}
