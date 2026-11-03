package com.example.arrax.ui.screen.business.customers.subscreen.view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.arrax.core.navigation.BusinessCustomerView
import com.example.arrax.domain.usecase.customer.DeleteCustomerUseCase
import com.example.arrax.domain.usecase.customer.Observe.ObserveCustomerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
@HiltViewModel
class CustomerViewViewmodel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeCustomer: ObserveCustomerUseCase,
    private val deleteCustomer: DeleteCustomerUseCase,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<BusinessCustomerView>()

    private val _state = MutableStateFlow(CustomerViewState())
    val state = _state.asStateFlow()

    init {
        observeCustomer(route.businessId, route.customerId)
            .onEach { c -> _state.update { it.copy(customer = c, isLoading = false) } }
            .catch { _state.update { it.copy(isLoading = false, message = "No se pudo cargar el cliente") } }
            .launchIn(viewModelScope)
    }

    fun onDeleteRequest() = _state.update { it.copy(showDeleteDialog = true) }
    fun onDeleteDismiss() = _state.update { it.copy(showDeleteDialog = false) }
    fun onMessageShown() = _state.update { it.copy(message = null) }

    fun onDeleteConfirm() {
        if (_state.value.isDeleting) return
        _state.update { it.copy(showDeleteDialog = false, isDeleting = true) }
        deleteCustomer(route.businessId, route.customerId)
            .onSuccess { _state.update { it.copy(isDeleting = false, deleted = true) } }
            .onFailure {
                _state.update { it.copy(isDeleting = false, message = "No se pudo eliminar") }
            }
    }
}