package com.example.arrax.ui.screen.business.members

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.InviteCode
import com.example.arrax.core.common.QrCodeGenerator
import com.example.arrax.core.common.toAppError
import com.example.arrax.core.navigation.BusinessMembers
import com.example.arrax.domain.model.Invitation
import com.example.arrax.domain.usecase.business.members.CreateInvitationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import com.example.arrax.core.common.NetworkMonitor

@HiltViewModel
class BusinessMembersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createInvitation: CreateInvitationUseCase,
    private val networkMonitor: NetworkMonitor, // ajusta el tipo/import al tuyo
) : ViewModel() {

    private val businessId = savedStateHandle.toRoute<BusinessMembers>().businessId

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
        if (_uiState.value.isGenerating) return
        viewModelScope.launch {
            _uiState.update { it.copy(isGenerating = true, inviteError = null) }
            // set().await() no termina sin red: evita dejar el botón colgado
            if (!networkMonitor.isConnected.first()) {
                _uiState.update {
                    it.copy(
                        isGenerating = false,
                        inviteError = "Necesitas conexión para crear una invitación."
                    )
                }
                return@launch
            }
            createInvitation(businessId)
                .onSuccess { inv ->
                    val qr = withContext(Dispatchers.Default) {
                        QrCodeGenerator.generate(InviteCode.deepLink(inv.code)).asImageBitmap()
                    }
                    _uiState.update { it.copy(isGenerating = false, invitation = inv.toUi(qr)) }
                }
                .onFailure { e ->
                    val msg = when (e.toAppError()) {
                        is AppError.PermissionDenied -> "No tienes permiso para invitar a este negocio."
                        is AppError.Network -> "No hay conexión a internet. Intenta de nuevo."
                        else -> "No se pudo crear la invitación. Intenta de nuevo."
                    }
                    _uiState.update { it.copy(isGenerating = false, inviteError = msg) }
                }
        }
    }

    private fun Invitation.toUi(qr: ImageBitmap): InvitationUi {
        val shown = InviteCode.display(code)
        val expires = SimpleDateFormat("d MMM yyyy, h:mm a", Locale("es", "MX"))
            .format(Date(expiresAt))
        return InvitationUi(
            code = shown,
            roleLabel = "Operador",
            expiresLabel = "Expira $expires",
            usesLabel = "Usos ilimitados",
            shareMessage = "Te invito a unirte a «$tenantName» en Arrax.\n\n" +
                    "Código: $shown\n" +
                    "En la app: Unirme → ingresa el código o escanea el QR.\n" +
                    "Válido hasta $expires.",
            qrImage = qr
        )
    }
}