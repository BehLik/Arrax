package com.example.arrax.ui.screen.business.members.subscreen.invitation

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.InviteCode
import com.example.arrax.core.common.NetworkMonitor
import com.example.arrax.core.common.QrCodeGenerator
import com.example.arrax.core.common.expiryLabel
import com.example.arrax.core.common.toAppError
import com.example.arrax.core.common.toLabel
import com.example.arrax.core.navigation.InvitationCreate
import com.example.arrax.domain.model.Invitation
import com.example.arrax.domain.model.InviteType
import com.example.arrax.domain.model.Role
import com.example.arrax.domain.usecase.business.members.create.CreateInvitationUseCase
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

@HiltViewModel
class InvitationCreateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createInvitation: CreateInvitationUseCase,
    private val networkMonitor: NetworkMonitor,
) : ViewModel() {

    private val businessId = savedStateHandle.toRoute<InvitationCreate>().businessId

    private val _uiState = MutableStateFlow(InvitationCreateState())
    val uiState: StateFlow<InvitationCreateState> = _uiState.asStateFlow()

    fun onTypeSelected(t: InviteType) = _uiState.update { it.copy(type = t) }
    fun onRoleSelected(r: Role) = _uiState.update { it.copy(role = r) }
    fun onPeopleChange(n: Int) = _uiState.update { it.copy(people = n.coerceIn(1, 10)) }

    fun onNext() {
        val s = _uiState.value
        if (s.isCreating) return
        viewModelScope.launch {
            _uiState.update { it.copy(isCreating = true, error = null) }
            // set().await() no termina sin red
            if (!networkMonitor.isConnected.first()) {
                _uiState.update {
                    it.copy(isCreating = false, error = "Necesitas conexión para crear una invitación.")
                }
                return@launch
            }
            createInvitation(businessId, s.role, s.type, s.people)
                .onSuccess { inv ->
                    val qr = if (s.type != InviteType.CODE) withContext(Dispatchers.Default) {
                        QrCodeGenerator.generate(InviteCode.deepLink(inv.code)).asImageBitmap()
                    } else null
                    _uiState.update {
                        it.copy(isCreating = false, step = InviteStep.Done, result = inv.toUi(qr))
                    }
                }
                .onFailure { e ->
                    val msg = when (e.toAppError()) {
                        is AppError.PermissionDenied -> "No tienes permiso para invitar a este negocio."
                        is AppError.Network -> "No hay conexión a internet. Intenta de nuevo."
                        else -> "No se pudo crear la invitación. Intenta de nuevo."
                    }
                    _uiState.update { it.copy(isCreating = false, error = msg) }
                }
        }
    }

    private fun Invitation.toUi(qr: ImageBitmap?): CreatedInvitationUi {
        val shown = InviteCode.display(code)
        val expires = SimpleDateFormat("d MMM yyyy, h:mm a", Locale("es", "MX")).format(
            Date(
                expiresAt
            )
        )
        return CreatedInvitationUi(
            type = type,
            typeLabel = type.toLabel(),
            roleLabel = role.toLabel(),
            peopleLabel = "$maxUses ${if (maxUses == 1) "persona" else "personas"}",
            expiresLabel = "${expiryLabel(expiresAt, System.currentTimeMillis())} · $expires",
            code = shown,
            shareMessage = "Te invito a unirte a «$tenantName» en Arrax.\n\n" +
                    "Código: $shown\n" +
                    "En la app: Unirme → ingresa el código o escanea el QR.\n" +
                    "Válido hasta $expires.",
            qrImage = qr
        )
    }
}

fun InviteType.toLabel() = when (this) {
    InviteType.CODE_QR -> "Código + QR"
    InviteType.CODE -> "Código"
    InviteType.QR -> "QR"
}