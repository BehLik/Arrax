package com.example.arrax.ui.screen.business.more

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class BusinessMoreViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(BusinessMoreState())
    val uiState: StateFlow<BusinessMoreState> = _uiState.asStateFlow()
}