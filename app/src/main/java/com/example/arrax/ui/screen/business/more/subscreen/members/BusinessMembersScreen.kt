package com.example.arrax.ui.screen.business.more.subscreen.members

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.display.ArxExpandableSection
import com.example.arrax.core.designsystem.components.display.ArxListItem
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.designsystem.components.feedback.ArxChipStatus
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.arrax.core.common.toLabel
import com.example.arrax.core.designsystem.access.RequiresPermission
import com.example.arrax.core.designsystem.components.buttons.ArxTextButton
import com.example.arrax.core.designsystem.components.feedback.ArxAlertDialog
import com.example.arrax.core.designsystem.icons.ArxIcons.Box
import com.example.arrax.domain.model.Permission
import com.example.arrax.domain.model.Role
import com.example.arrax.domain.model.assignableRoles
import com.example.arrax.ui.screen.business.more.subscreen.members.component.InviteMemberButton
import com.example.arrax.ui.screen.business.more.subscreen.members.component.MemberRow
import com.example.arrax.ui.screen.business.more.subscreen.members.component.chipFamily

@Composable
fun BusinessMembersScreen(
    onBack: () -> Unit,
    onCreateInvitationClick: () -> Unit,
    modifier: Modifier = Modifier,
    onInvitationClick: (String) -> Unit = {},
    viewModel: BusinessMembersViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BusinessMembersContent(
        state = state,
        onBack = onBack,
        onCreateInvitationClick = onCreateInvitationClick,
        onMemberClick = viewModel::onMemberClick,
        onInvitationClick = onInvitationClick,
        onToggleActive = viewModel::onToggleActive,
        onToggleExpired = viewModel::onToggleExpired,
        onChangeRole = viewModel::onChangeRole,
        onRemoveClick = viewModel::onRemoveClick,
        onConfirmRemove = viewModel::onConfirmRemove,
        onDismissRemove = viewModel::onDismissRemove,
        onDismissMember = viewModel::onDismissMember,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BusinessMembersContent(
    state: BusinessMembersState,
    onBack: () -> Unit,
    onCreateInvitationClick: () -> Unit,
    onMemberClick: (String) -> Unit,
    onInvitationClick: (String) -> Unit,
    onToggleActive: () -> Unit,
    onToggleExpired: () -> Unit,
    onChangeRole: (Role) -> Unit,
    onRemoveClick: () -> Unit,
    onConfirmRemove: () -> Unit,
    onDismissRemove: () -> Unit,
    onDismissMember: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArxBackground(modifier = modifier, decoration = ArxDecoration.None) {
        Column(modifier = Modifier.fillMaxSize()) {
            ArxTopBar(
                title = "Equipo y permisos",
                centeredTitle = false,
                navigationType = ArxTopBarNavigationType.BACK,
                onNavigationClick = onBack
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = ArxSpacing.md, vertical = ArxSpacing.xs),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
            ) {
                item(key = "subtitle") {
                    Text(
                        "Gestiona quién puede acceder a tu negocio.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                item(key = "create") {
                    RequiresPermission(Permission.INVITE_CREATE) {
                    InviteMemberButton(
                        onClick = onCreateInvitationClick,
                        modifier = Modifier.padding(vertical = ArxSpacing.sm)
                    )}
                }
                item(key = "active") {
                    RequiresPermission(Permission.INVITE_CREATE) {
                        InvitationsSection(
                            title = "Invitaciones activas",
                            items = state.activeInvitations,
                            expanded = state.activeExpanded,
                            emptyText = "No hay invitaciones activas.",
                            onToggle = onToggleActive,
                            onClick = onInvitationClick
                        )
                    }
                }
                item(key = "expired") {
                    RequiresPermission(Permission.INVITE_CREATE) {
                        InvitationsSection(
                            title = "Invitaciones vencidas",
                            items = state.expiredInvitations,
                            expanded = state.expiredExpanded,
                            emptyText = "No hay invitaciones vencidas.",
                            onToggle = onToggleExpired,
                            onClick = onInvitationClick
                        )
                    }
                }
                item(key = "members-header") {
                    Text(
                        "${state.members.size} integrantes",
                        modifier = Modifier.padding(top = ArxSpacing.sm, bottom = ArxSpacing.xs),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                when {
                    state.isLoadingMembers -> item(key = "members-loading") { ArxLoading() }
                    state.membersError != null -> item(key = "members-error") {
                        Text(
                            state.membersError,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    else -> {
                        val grouped = state.members.groupBy { it.role }
                        listOf(Role.OWNER, Role.ADMIN, Role.OPERATOR).forEach { role ->
                            val list = grouped[role].orEmpty()
                            if (list.isNotEmpty()) {
                                item(key = "hdr-${role.name}") { RoleHeader(role, list.size) }
                                items(list, key = { it.id }) { MemberRow(member = it, onClick = onMemberClick) }
                            }
                        }
                    }
                }
            }
        }
    }
    state.selectedMember?.let { m ->
        MemberActionsSheet(
            member = m,
            assignable = state.myRole?.let { assignableRoles(it, m.role) }.orEmpty(),
            isWorking = state.isWorking,
            error = state.actionError,
            onChangeRole = onChangeRole,
            onRemove = onRemoveClick,
            onDismiss = onDismissMember
        )
        if (state.confirmRemove) {
            ArxAlertDialog(
                title = "Quitar del negocio",
                message = "${m.name} perderá el acceso a este negocio. Podrá volver a unirse con una nueva invitación.",
                confirmText = "Quitar",
                dismissText = "Volver",
                isDestructive = true,
                onConfirm = onConfirmRemove,
                onDismiss = onDismissRemove
            )
        }
    }
}

@Composable
private fun EmptyHint(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = ArxSpacing.xs)
    )
}
@Composable
internal fun InvitationsSection(
    title: String,
    items: List<InvitationItemUi>,
    expanded: Boolean,
    emptyText: String,
    onToggle: () -> Unit,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ArxExpandableSection(
        title = title,
        count = items.size,
        expanded = expanded,
        onToggle = onToggle,
        modifier = modifier
    ) {
        if (items.isEmpty()) {
            Text(
                emptyText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = ArxSpacing.xs)
            )
        }
        items.forEach { InvitationRow(item = it, onClick = onClick) }
    }
}
@Composable
internal fun InvitationRow(
    item: InvitationItemUi,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ArxListItem(
        title = item.typeLabel,
        subtitle = "${item.roleLabel} · ${item.details}",
        modifier = modifier,
        showChevron = true,
        leading = { ArxIcon(icon = ArxIcons.ScanQr, tint = MaterialTheme.colorScheme.primary) },
        trailing = {
            if (item.isActive) ArxStatusChip(text = "Activa", status = ArxChipStatus.Success)
            else ArxStatusChip(text = "Vencida", status = ArxChipStatus.Info)
        },
        onClick = { onClick(item.code) }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MemberActionsSheet(
    member: MemberUi,
    assignable: List<Role>,
    isWorking: Boolean,
    error: String?,
    onChangeRole: (Role) -> Unit,
    onRemove: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.padding(horizontal = ArxSpacing.md).padding(bottom = ArxSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
        ) {
            MemberRow(member = member.copy(canEdit = false), onClick = {})
            if (assignable.isNotEmpty()) {
                Text("Cambiar rol", style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = ArxSpacing.sm))
                assignable.forEach { role ->
                    ArxListItem(
                        title = role.toLabel(),
                        subtitle = if (role == Role.ADMIN) "Gestiona eventos, clientes y usuarios."
                        else "Registra y opera el día a día.",
                        showChevron = false,
                        leading = { ArxIcon(icon = ArxIcons.User, tint = role.chipFamily().color) },
                        onClick = { if (!isWorking) onChangeRole(role) }
                    )
                }
            }
            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            ArxTextButton(text = "Quitar del negocio", onClick = onRemove,
                isDestructive = true, enabled = !isWorking)
        }
    }
}
@Composable
private fun RoleHeader(role: Role, count: Int) {
    val label = when (role) {
        Role.OWNER -> "Propietario"
        Role.ADMIN -> "Administradores"
        else -> "Operadores"
    }
    Row(
        modifier = Modifier.padding(top = ArxSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
    ) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(role.chipFamily().color))
        Text("$label · $count", style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
//@Preview(showBackground = true, widthDp = 360, heightDp = 780)
//@Composable
//private fun BusinessMembersPreview() {
//    ArxTheme(darkTheme = true) {
//        BusinessMembersContent(
//            state = BusinessMembersState(
//                isLoadingMembers = false,
//                activeInvitations = listOf(
//                    InvitationItemUi("A1", "Código + QR", "Operador", "2 de 5 personas · Expira en 3 días", isActive = true, statusLabel = "dd")
//                ),
//                members = listOf(
//                    MemberUi("1", "Alex Caamal", "AC", "Propietario", isOwner = true),
//                    MemberUi("2", "Carlos Ramírez", "CR", "Operador")
//                )
//            ),
//            onBack = {}, onCreateInvitationClick = {}, onMemberClick = {},
//            onInvitationClick = {}, onToggleActive = {}, onToggleExpired = {}
//        )
//    }
//}