package com.example.arrax.ui.screen.business.customers.subscreen.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.arrax.core.navigation.BusinessCustomerAdd
import com.example.arrax.domain.model.Customer
import com.example.arrax.domain.usecase.customer.Observe.ObserveCustomerUseCase
import com.example.arrax.domain.usecase.customer.SaveCustomerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerAddViewmodel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeCustomer: ObserveCustomerUseCase,
    private val saveCustomer: SaveCustomerUseCase,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<BusinessCustomerAdd>()

    private val _state = MutableStateFlow(
        CustomerAddState(customerId = route.customerId, isLoading = route.customerId != null)
    )
    val state = _state.asStateFlow()

    init {
        route.customerId?.let { id ->
            viewModelScope.launch {
                val c = runCatching { observeCustomer(route.businessId, id).first() }.getOrNull()
                _state.update {
                    if (c == null) it.copy(isLoading = false, message = "No se encontró el cliente")
                    else it.copy(
                        isLoading = false,
                        name = c.name, phone = c.phone, address = c.address,
                        reference = c.reference, photo = c.photoUrl,
                    )
                }
            }
        }
    }

    fun onNameChange(v: String) = _state.update { it.copy(name = v, nameError = null) }
    fun onPhoneChange(v: String) = _state.update { it.copy(phone = v) }
    fun onAddressChange(v: String) = _state.update { it.copy(address = v) }
    fun onReferenceChange(v: String) = _state.update { it.copy(reference = v) }
    fun onPhotoPicked(uri: String) = _state.update { it.copy(photo = uri) }
    fun onMessageShown() = _state.update { it.copy(message = null) }

    fun onSave() {
        val s = _state.value
        if (s.isSaving || s.finished || s.isLoading) return
        if (s.name.isBlank()) {
            _state.update { it.copy(nameError = "Escribe el nombre del cliente") }
            return
        }
        val customer = Customer(
            id = s.customerId.orEmpty(),
            name = s.name,
            phone = s.phone,
            address = s.address,
            reference = s.reference,
            // Una uri local (content://) no es una URL: hasta subirla a Storage, no se guarda.
            photoUrl = s.photo?.takeIf { it.startsWith("http") },
        )
        saveCustomer(route.businessId, customer)
            .onSuccess { _state.update { it.copy(finished = true) } }
            .onFailure { _state.update { it.copy(message = "No se pudo guardar el cliente") } }
    }
}