package com.example.arrax.ui.screen.business.members

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class BusinessMembersViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(BusinessMembersState())
    val uiState: StateFlow<BusinessMembersState> = _uiState.asStateFlow()

    fun onInviteClick() {
        _uiState.update { it.copy(isInviteSheetVisible = true) }
    }

    fun onDismissInvite() {
        _uiState.update { it.copy(isInviteSheetVisible = false) }
    }

    fun onInviteTabSelected(tab: InviteTab) {
        _uiState.update { it.copy(inviteTab = tab) }
    }

    fun onGenerateInvitation() {
        // TODO: llamar al repositorio/caso de uso que genera la invitación.
    }
}