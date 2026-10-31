package com.example.arrax.ui.screen.business.members.subscreen.invitationdetail

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
import com.example.arrax.core.common.minuteTicker
import com.example.arrax.core.common.toLabel
import com.example.arrax.core.navigation.InvitationDetail
import com.example.arrax.domain.model.Invitation
import com.example.arrax.domain.model.InviteStatus
import com.example.arrax.domain.model.InviteType
import com.example.arrax.domain.model.status
import com.example.arrax.domain.usecase.business.members.delete.DeleteInvitationUseCase
import com.example.arrax.domain.usecase.business.members.observe.ObserveInvitationUseCase
import com.example.arrax.domain.usecase.business.members.observe.ObserveMembersUseCase
import com.example.arrax.domain.usecase.business.members.revoke.RevokeInvitationUseCase
import com.example.arrax.ui.screen.business.members.component.InvitationDetailEvent
import com.example.arrax.ui.screen.business.members.component.InvitationDetailState
import com.example.arrax.ui.screen.business.members.component.InvitationDetailUi
import com.example.arrax.ui.screen.business.members.component.InviteConfirm
import com.example.arrax.ui.screen.business.members.subscreen.invitation.toLabel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import com.example.arrax.core.common.toAppError
import com.example.arrax.ui.screen.business.members.component.toMemberUi

@HiltViewModel
class MembersInvitationDetailViewmodel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeInvitation: ObserveInvitationUseCase,
    observeMembers: ObserveMembersUseCase,
    private val revokeInvitation: RevokeInvitationUseCase,
    private val deleteInvitation: DeleteInvitationUseCase,
    private val networkMonitor: NetworkMonitor,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<InvitationDetail>()

    private val _uiState = MutableStateFlow(InvitationDetailState())
    val uiState: StateFlow<InvitationDetailState> = _uiState.asStateFlow()

    private val _events = Channel<InvitationDetailEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        combine(
            observeInvitation(route.code),
            observeMembers(route.businessId),
            minuteTicker()
        ) { inv, members, now ->
            inv?.toDetailUi(now) to members
                .filter { it.inviteCode == route.code }
                .sortedBy { it.joinedAt }
                .map { it.toMemberUi() }
        }
            .onEach { (inv, guests) ->
                _uiState.update { it.copy(isLoading = false, invitation = inv, guests = guests) }
            }
            .catch { e ->
                _uiState.update { it.copy(isLoading = false, error = e.toAppError().toMessage()) }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            val qr = withContext(Dispatchers.Default) {
                QrCodeGenerator.generate(InviteCode.deepLink(route.code)).asImageBitmap()
            }
            _uiState.update { it.copy(qrImage = qr) }
        }
    }



    fun onRevokeClick() = _uiState.update { it.copy(confirm = InviteConfirm.Revoke) }
    fun onDeleteClick() = _uiState.update { it.copy(confirm = InviteConfirm.Delete) }
    fun onDismissConfirm() = _uiState.update { it.copy(confirm = null) }

    fun onConfirm() {
        val action = _uiState.value.confirm ?: return
        if (_uiState.value.isWorking) return
        viewModelScope.launch {
            _uiState.update { it.copy(confirm = null, isWorking = true, error = null) }
            // update()/delete().await() no terminan sin red
            if (!networkMonitor.isConnected.first()) {
                _uiState.update { it.copy(isWorking = false, error = "Necesitas conexión para continuar.") }
                return@launch
            }
            val result = when (action) {
                InviteConfirm.Revoke -> revokeInvitation(route.code)
                InviteConfirm.Delete -> deleteInvitation(route.code)
            }
            result
                .onSuccess {
                    _uiState.update { it.copy(isWorking = false) }
                    if (action == InviteConfirm.Delete) _events.send(InvitationDetailEvent.Deleted)
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isWorking = false, error = e.toAppError().toMessage()) }
                }
        }
    }

    private fun AppError.toMessage() = when (this) {
        is AppError.PermissionDenied -> "No tienes permiso para modificar esta invitación."
        is AppError.Network -> "No hay conexión a internet. Intenta de nuevo."
        else -> "No se pudo completar la acción. Intenta de nuevo."
    }

    private fun Invitation.toDetailUi(now: Long): InvitationDetailUi {
        val s = status(now)
        val shown = InviteCode.display(code)
        val date = SimpleDateFormat("d MMM yyyy, h:mm a", Locale("es", "MX")).format(Date(expiresAt))
        return InvitationDetailUi(
            code = shown,
            typeLabel = type.toLabel(),
            roleLabel = role.toLabel(),
            usesLabel = "$usedCount de $maxUses ${if (maxUses == 1) "persona" else "personas"}",
            expiresLabel = if (s == InviteStatus.REVOKED) "Cancelada" else "${expiryLabel(expiresAt, now)} · $date",
            status = s,
            statusLabel = when (s) {
                InviteStatus.ACTIVE -> "Activa"
                InviteStatus.FULL -> "Agotada"
                InviteStatus.EXPIRED -> "Vencida"
                InviteStatus.REVOKED -> "Cancelada"
            },
            showCode = type != InviteType.QR,
            showQr = type != InviteType.CODE,
            shareMessage = "Te invito a unirte a «$tenantName» en Arrax.\n\n" +
                    "Código: $shown\n" +
                    "En la app: Unirme → ingresa el código o escanea el QR.\n" +
                    "Válido hasta $date."
        )
    }
}