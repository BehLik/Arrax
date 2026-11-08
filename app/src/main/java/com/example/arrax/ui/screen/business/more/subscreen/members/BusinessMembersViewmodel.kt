package com.example.arrax.ui.screen.business.more.subscreen.members

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.NetworkMonitor
import com.example.arrax.core.common.expiryLabel
import com.example.arrax.core.common.toAppError
import com.example.arrax.core.common.toLabel
import com.example.arrax.core.navigation.BusinessMembers
import com.example.arrax.domain.model.Access
import com.example.arrax.domain.model.Invitation
import com.example.arrax.domain.model.InviteStatus
import com.example.arrax.domain.model.Member
import com.example.arrax.domain.model.Permission
import com.example.arrax.domain.model.Role
import com.example.arrax.domain.model.canManageMember
import com.example.arrax.domain.model.status
import com.example.arrax.domain.usecase.access.ObserveAccessUseCase
import com.example.arrax.domain.usecase.business.members.manage.RemoveMemberUseCase
import com.example.arrax.domain.usecase.business.members.manage.UpdateMemberRoleUseCase
import com.example.arrax.domain.usecase.business.members.observe.ObserveInvitationsUseCase
import com.example.arrax.domain.usecase.business.members.observe.ObserveMembersUseCase
import com.example.arrax.domain.usecase.user.ObserveCurrentUserUseCase
import com.example.arrax.ui.screen.business.more.subscreen.members.subscreen.invitation.toLabel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BusinessMembersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeMembers: ObserveMembersUseCase,
    private val observeInvitations: ObserveInvitationsUseCase,
    private val observeAccess: ObserveAccessUseCase,
    private val observeCurrentUser: ObserveCurrentUserUseCase,
    private val updateMemberRole: UpdateMemberRoleUseCase,
    private val removeMember: RemoveMemberUseCase,
    private val networkMonitor: NetworkMonitor,
) : ViewModel() {

    private val businessId = savedStateHandle.toRoute<BusinessMembers>().businessId

    private val _uiState = MutableStateFlow(BusinessMembersState())
    val uiState: StateFlow<BusinessMembersState> = _uiState.asStateFlow()

    private val clock = flow {
        while (true) { emit(System.currentTimeMillis()); delay(60_000) }
    }
    private val joinedFormat = SimpleDateFormat("MMM yyyy", Locale("es", "MX"))

    init {
        combine(
            observeMembers(businessId),
            observeCurrentUser().map { it?.uid },
            observeAccess(businessId)
        ) { list, myUid, access ->
            val myRole = (access as? Access.Granted)?.role
            list.sortedWith(compareBy<Member>({ roleOrder(it.role) }, { it.joinedAt }))
                .map { it.toMemberUi(myUid, myRole) }
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
            .launchIn(viewModelScope)

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

        observeAccess(businessId)
            .onEach { a ->
                val g = a as? Access.Granted
                _uiState.update {
                    it.copy(canManage = g?.can(Permission.MEMBERS_MANAGE) == true, myRole = g?.role)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onToggleActive() = _uiState.update { it.copy(activeExpanded = !it.activeExpanded) }
    fun onToggleExpired() = _uiState.update { it.copy(expiredExpanded = !it.expiredExpanded) }
    fun onMemberClick(id: String) = _uiState.update { it.copy(selectedId = id, actionError = null) }
    fun onDismissMember() = _uiState.update { it.copy(selectedId = null, confirmRemove = false, actionError = null) }
    fun onRemoveClick() = _uiState.update { it.copy(confirmRemove = true) }
    fun onDismissRemove() = _uiState.update { it.copy(confirmRemove = false) }
    fun onChangeRole(role: Role) = runAction { uid -> updateMemberRole(businessId, uid, role) }
    fun onConfirmRemove() = runAction { uid -> removeMember(businessId, uid) }

    private fun runAction(block: suspend (String) -> Result<Unit>) {
        val id = _uiState.value.selectedId ?: return
        if (_uiState.value.isWorking) return
        viewModelScope.launch {
            _uiState.update { it.copy(confirmRemove = false, isWorking = true, actionError = null) }
            // update()/delete().await() no terminan sin red
            if (!networkMonitor.isConnected.first()) {
                _uiState.update { it.copy(isWorking = false, actionError = "Necesitas conexión para continuar.") }
                return@launch
            }
            block(id)
                .onSuccess { _uiState.update { it.copy(isWorking = false, selectedId = null) } }
                .onFailure { e ->
                    val msg = when (e.toAppError()) {
                        is AppError.PermissionDenied -> "No tienes permiso para modificar a este miembro."
                        is AppError.Network -> "No hay conexión a internet. Intenta de nuevo."
                        else -> "No se pudo completar la acción. Intenta de nuevo."
                    }
                    _uiState.update { it.copy(isWorking = false, actionError = msg) }
                }
        }
    }

    private fun roleOrder(r: Role) = when (r) { Role.OWNER -> 0; Role.ADMIN -> 1; else -> 2 }

    private fun Member.toMemberUi(myUid: String?, myRole: Role?) = MemberUi(
        id = uid,
        name = name,
        initials = name.split(" ").filter { it.isNotBlank() }.take(2)
            .joinToString("") { it.first().uppercase() }.ifEmpty { "?" },
        role = role,
        roleLabel = role.toLabel(),
        joinedLabel = "Miembro desde ${joinedFormat.format(Date(joinedAt))}",
        isCurrentUser = uid == myUid,
        canEdit = uid != myUid && myRole != null && canManageMember(myRole, role)
    )

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
}