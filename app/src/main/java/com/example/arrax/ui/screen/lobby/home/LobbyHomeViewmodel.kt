package com.example.arrax.ui.screen.lobby.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.NetworkMonitor
import com.example.arrax.domain.usecase.business.ObserveMyBusinessesUseCase
import com.example.arrax.domain.usecase.user.ObserveCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbyHomeViewModel @Inject constructor(
    private val observeMyBusinessesUseCase: ObserveMyBusinessesUseCase,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {
    companion object { private const val PAGE_SIZE = 4 }
    private val _state = MutableStateFlow(LobbyHomeState())
    val state: StateFlow<LobbyHomeState> = _state.asStateFlow()
    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            try {
                // TODO: aquí va tu caso de uso de refresco (ej. tenantRepository.refreshMyBusinesses())
                delay(600)
            } finally {
                _state.update { it.copy(isRefreshing = false) }
            }
        }
    }
    init {
        viewModelScope.launch {
            // combine does NOT resubscribe the Firestore flows when connectivity changes;
            // it only re-emits the latest values, so no flatMapLatest is needed here.
            combine(
                observeCurrentUserUseCase(),
                observeMyBusinessesUseCase().catch { emit(emptyList()) },
                networkMonitor.isConnected
            ) {  user, businesses, online -> Triple(user, businesses, online) }
                .collect { (user, businesses, online) ->
                    _state.update {
                        it.copy(
                            userName = user?.name.orEmpty(),
                            recentBusinesses = businesses.map { b ->
                                BusinessItem(
                                    id = b.tenant.id,
                                    name = b.tenant.name,
                                    isAdmin = b.isAdmin,
                                    totalEvents = b.totalEvents
                                )
                            },
                            isLoading = false,
                            isOffline = !online
                        )
                    }
                    val items = businesses.map { b ->
                        BusinessItem(id = b.tenant.id, name = b.tenant.name, isAdmin = b.isAdmin, totalEvents = b.totalEvents)
                    }
                    _state.update {
                        it.copy(
                            userName = user?.name.orEmpty(),
                            recentBusinesses = items.take(PAGE_SIZE),
                            hasMoreBusinesses = items.size > PAGE_SIZE,
                            isLoading = false,
                            isOffline = !online
                        )
                    }
                }
        }
    }
}
