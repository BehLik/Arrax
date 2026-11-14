package com.example.arrax.ui.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.navigation.AuthGraph
import com.example.arrax.core.navigation.LobbyGraph
import com.example.arrax.ui.root.startup.LastContextStore
import com.example.arrax.ui.root.startup.StartTarget
import com.example.arrax.ui.root.startup.StartupResolver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * root     = grafo con el que arranca el NavHost (Auth o Lobby)
 * restore  = si no es null, hay que armar la pila encima del Lobby
 *            (Lobby -> Business -> Event) antes de mostrarla
 */
data class StartState(
    val root: Any,
    val needsVerification: Boolean = false,
    val restore: StartTarget? = null
)

@HiltViewModel
class AppStartViewModel @Inject constructor(
    private val resolver: StartupResolver,
    private val lastContext: LastContextStore
) : ViewModel() {

    private val _start = MutableStateFlow<StartState?>(null)
    val start: StateFlow<StartState?> = _start.asStateFlow()

    init {
        viewModelScope.launch {
            _start.value = when (val target = resolver.resolve()) {
                StartTarget.Auth -> StartState(AuthGraph)
                StartTarget.VerifyEmail -> StartState(AuthGraph, needsVerification = true)
                StartTarget.Lobby -> StartState(LobbyGraph)
                is StartTarget.Business,
                is StartTarget.Event -> StartState(LobbyGraph, restore = target)
            }
        }
    }

    fun saveContext(businessId: String, eventId: String?) {
        viewModelScope.launch { lastContext.save(businessId, eventId) }
    }

    fun clearContext() {
        viewModelScope.launch { lastContext.clear() }
    }
}