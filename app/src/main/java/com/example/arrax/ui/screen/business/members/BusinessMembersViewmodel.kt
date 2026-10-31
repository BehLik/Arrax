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
import com.example.arrax.core.common.NetworkMonitor
import com.example.arrax.core.common.expiryLabel
import com.example.arrax.core.common.toLabel
import com.example.arrax.domain.model.InviteStatus
import com.example.arrax.domain.model.Member
import com.example.arrax.domain.model.Role
import com.example.arrax.domain.model.status
import com.example.arrax.domain.usecase.business.members.observe.ObserveInvitationsUseCase
import com.example.arrax.domain.usecase.business.members.observe.ObserveMembersUseCase
import com.example.arrax.ui.screen.business.members.subscreen.invitation.toLabel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import com.example.arrax.core.common.toLabel
@HiltViewModel
class BusinessMembersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeMembers: ObserveMembersUseCase,
    private val observeInvitations: ObserveInvitationsUseCase,
) : ViewModel() {

    private val businessId = savedStateHandle.toRoute<BusinessMembers>().businessId

    private val _uiState = MutableStateFlow(BusinessMembersState())
    val uiState: StateFlow<BusinessMembersState> = _uiState.asStateFlow()

    private val clock = flow {
        while (true) { emit(System.currentTimeMillis()); delay(60_000) }
    }

    init {
        observeMembers(businessId)
            .map { list ->
                list.sortedWith(
                    compareByDescending<Member> { it.role == Role.OWNER }.thenBy { it.joinedAt }
                ).map { it.toMemberUi() }
            }
            .onEach { ui ->
                _uiState.update { it.copy(members = ui, isLoadingMembers = false, membersError = null) }
            }
            .catch { e ->
                _uiState.update {
                    it.copy(
                        isLoadingMembers = false,
                        membersError = when (e.toAppError()) {
                            is AppError.PermissionDenied -> "No tienes permiso para ver el equipo."
                            is AppError.Network -> "No hay conexión a internet."
                            else -> "No se pudo cargar el equipo."
                        }
                    )
                }
            }
            .launchIn(viewModelScope)          // <- faltaba

        combine(observeInvitations(businessId), clock) { list, now ->
            val (active, expired) = list
                .sortedByDescending { it.createdAt }
                .partition { it.status(now) == InviteStatus.ACTIVE }
            active.map { it.toItem(now) } to expired.map { it.toItem(now) }
        }
            .onEach { (active, expired) ->
                _uiState.update { it.copy(activeInvitations = active, expiredInvitations = expired) }
            }
            .catch { /* sin permiso o sin red: se queda la lista vacía */ }
            .launchIn(viewModelScope)
    }

    fun onToggleActive() = _uiState.update { it.copy(activeExpanded = !it.activeExpanded) }
    fun onToggleExpired() = _uiState.update { it.copy(expiredExpanded = !it.expiredExpanded) }

//    private fun Invitation.toItem(now: Long, active: Boolean) = InvitationItemUi(
//        code = code,
//        typeLabel = type.toLabel(),
//        roleLabel = role.toLabel(),
//        details = buildString {
//            append("$usedCount de $maxUses ${if (maxUses == 1) "persona" else "personas"}")
//            append(" · ")
//            append(if (revoked) "Revocada" else expiryLabel(expiresAt, now))
//        },
//        isActive = active
//    )
private fun Invitation.toItem(now: Long): InvitationItemUi {
    val s = status(now)
    val people = "$usedCount de $maxUses ${if (maxUses == 1) "persona" else "personas"}"
    return InvitationItemUi(
        code = code,
        typeLabel = type.toLabel(),
        roleLabel = role.toLabel(),
        details = "$people · " + when (s) {
            InviteStatus.ACTIVE, InviteStatus.EXPIRED -> expiryLabel(expiresAt, now)
            InviteStatus.FULL -> "Cupo completo"
            InviteStatus.REVOKED -> "Cancelada"
        },
        statusLabel = when (s) {
            InviteStatus.ACTIVE -> "Activa"
            InviteStatus.FULL -> "Agotada"
            InviteStatus.EXPIRED -> "Vencida"
            InviteStatus.REVOKED -> "Cancelada"
        },
        isActive = s == InviteStatus.ACTIVE
    )
}

    fun Member.toMemberUi() = MemberUi(
        id = uid,
        name = name,
        initials = name.split(" ").filter { it.isNotBlank() }.take(2)
            .joinToString("") { it.first().uppercase() }.ifEmpty { "?" },
        roleLabel = role.toLabel(),
        isOwner = role == Role.OWNER
    )
}