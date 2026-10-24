package com.example.arrax.ui.screen.business.alerts

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class BusinessAlertsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(BusinessAlertsState())
    val uiState: StateFlow<BusinessAlertsState> = _uiState.asStateFlow()

    fun onTabSelected(tab: AlertsTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun onMarkAllRead() {
        _uiState.update { state ->
            state.copy(alerts = state.alerts.map { it.copy(isUnread = false) })
        }
    }
}