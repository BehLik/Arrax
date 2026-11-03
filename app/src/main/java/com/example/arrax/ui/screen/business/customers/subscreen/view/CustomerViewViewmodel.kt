package com.example.arrax.ui.screen.business.customers.subscreen.view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.arrax.core.navigation.BusinessCustomerView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewViewmodel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<BusinessCustomerView>()

    private val _state = MutableStateFlow(CustomerViewState())
    val state = _state.asStateFlow()

    init {
        // TODO(data): observar el cliente route.customerId y hacer
        // _state.update { it.copy(customer = c, isLoading = false) }
        // (mientras no exista, la pantalla se queda en el indicador de carga)
    }

    fun onDeleteRequest() = _state.update { it.copy(showDeleteDialog = true) }
    fun onDeleteDismiss() = _state.update { it.copy(showDeleteDialog = false) }
    fun onMessageShown() = _state.update { it.copy(message = null) }

    fun onDeleteConfirm() {
        if (_state.value.isDeleting) return
        _state.update { it.copy(showDeleteDialog = false, isDeleting = true) }
        viewModelScope.launch {
            // TODO(data): eliminar route.customerId. En error:
            // _state.update { it.copy(isDeleting = false, message = "No se pudo eliminar") }
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }
}