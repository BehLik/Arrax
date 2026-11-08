package com.example.arrax.ui.screen.business.access

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.domain.model.Access
import com.example.arrax.domain.usecase.access.ObserveAccessUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class BusinessAccessViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeAccess: ObserveAccessUseCase
) : ViewModel() {

    private val businessId: String = checkNotNull(savedStateHandle["businessId"]) {
        "La ruta debe tener el argumento businessId"
    }

    val access: StateFlow<Access> = observeAccess(businessId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Access.Loading)
}