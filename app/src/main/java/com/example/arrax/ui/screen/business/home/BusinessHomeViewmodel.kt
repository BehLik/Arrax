package com.example.arrax.ui.screen.business.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.arrax.core.navigation.BusinessHome
import com.example.arrax.domain.model.Role
import com.example.arrax.domain.usecase.business.members.ObserveMembersUseCase
import com.example.arrax.domain.usecase.tenant.observe.ObserveTenantUseCase
import com.example.arrax.domain.usecase.user.ObserveCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@HiltViewModel
class BusinessHomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeTenant: ObserveTenantUseCase,
    observeMembers: ObserveMembersUseCase,
    observeCurrentUser: ObserveCurrentUserUseCase,
) : ViewModel() {

    private val businessId = savedStateHandle.toRoute<BusinessHome>().businessId

    private val _uiState = MutableStateFlow(BusinessHomeState(isLoading = true))
    val uiState: StateFlow<BusinessHomeState> = _uiState.asStateFlow()

    init {
        combine(
            observeTenant(businessId),
            observeMembers(businessId),
            observeCurrentUser()
        ) { tenant, members, user ->
            val myRole = members.firstOrNull { it.uid == user?.uid }?.role
            Triple(tenant?.name.orEmpty(), myRole, members.size)
        }
            .onEach { (name, role, count) ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        businessName = name,
                        roleLabel = role?.toLabel().orEmpty(),
                        memberCount = count
                    )
                }
            }
            .catch { _uiState.update { it.copy(isLoading = false) } }
            .launchIn(viewModelScope)
    }
}

private fun Role.toLabel() = when (this) {
    Role.OWNER -> "Propietario"
    Role.OPERATOR -> "Operador"
    else -> "Administrador"
}