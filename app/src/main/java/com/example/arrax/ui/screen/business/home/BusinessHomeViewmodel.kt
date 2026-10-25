package com.example.arrax.ui.screen.business.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class BusinessHomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(BusinessHomeState())
    val uiState: StateFlow<BusinessHomeState> = _uiState.asStateFlow()
}