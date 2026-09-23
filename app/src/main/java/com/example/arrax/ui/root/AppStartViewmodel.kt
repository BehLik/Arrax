package com.example.arrax.ui.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.navigation.Routes
import com.example.arrax.data.local.SesionLocalDataSource
import com.example.arrax.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppStartViewmodel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sesionLocalDataSource: SesionLocalDataSource
) : ViewModel() {

    private val _destinoInicial = MutableStateFlow<Routes?>(null)
    val destinoInicial: StateFlow<Routes?> = _destinoInicial.asStateFlow()

    init {
        viewModelScope.launch {
            val usuario = authRepository.observeCurrentUser().first()
            _destinoInicial.value = when {
                usuario == null -> Routes.Login
                sesionLocalDataSource.tenantId.first() != null -> Routes.LobbyHome
                else -> Routes.LobbyOnboarding
            }
        }
    }
}