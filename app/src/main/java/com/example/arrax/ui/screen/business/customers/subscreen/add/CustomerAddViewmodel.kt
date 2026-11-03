package com.example.arrax.ui.screen.business.customers.subscreen.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.arrax.core.navigation.BusinessCustomerAdd
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerAddViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<BusinessCustomerAdd>()

    private val _state = MutableStateFlow(CustomerAddState(customerId = route.customerId))
    val state = _state.asStateFlow()

    init {
        route.customerId?.let { id ->
            // TODO(data): observar/cargar el cliente `id` y volcarlo al formulario:
            // _state.update { it.copy(name = c.name, phone = c.phone, address = c.address,
            //                         reference = c.reference, photo = c.photoUrl) }
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
        if (s.isSaving) return
        if (s.name.isBlank()) {
            _state.update { it.copy(nameError = "Escribe el nombre del cliente") }
            return
        }
        _state.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            // TODO(data): crear o actualizar (según s.customerId) con name/phone/address/reference
            // y subir s.photo a Storage si es una uri local. En error:
            // _state.update { it.copy(isSaving = false, message = "No se pudo guardar el cliente") }
            _state.update { it.copy(isSaving = false, finished = true) }
        }
    }
}