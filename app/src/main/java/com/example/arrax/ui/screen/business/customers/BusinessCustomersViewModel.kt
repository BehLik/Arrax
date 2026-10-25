package com.example.arrax.ui.screen.business.customers

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class BusinessCustomersViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(BusinessCustomersState())
    val uiState: StateFlow<BusinessCustomersState> = _uiState.asStateFlow()

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun onTabSelected(tab: CustomersTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
}