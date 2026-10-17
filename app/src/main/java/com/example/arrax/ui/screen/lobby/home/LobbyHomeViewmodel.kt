package com.example.arrax.ui.screen.lobby.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.NetworkMonitor
import com.example.arrax.domain.usecase.business.ObserveMyBusinessesUseCase
import com.example.arrax.domain.usecase.user.ObserveCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _state = MutableStateFlow(LobbyHomeState())
    val state: StateFlow<LobbyHomeState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            // combine does NOT resubscribe the Firestore flows when connectivity changes;
            // it only re-emits the latest values, so no flatMapLatest is needed here.
            combine(
                observeCurrentUserUseCase(),
                observeMyBusinessesUseCase(),
                networkMonitor.isConnected
            ) { user, businesses, online -> Triple(user, businesses, online) }
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
                }
        }
    }
}
