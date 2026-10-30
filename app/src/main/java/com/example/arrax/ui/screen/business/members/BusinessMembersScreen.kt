package com.example.arrax.ui.screen.business.members

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.example.arrax.ui.screen.business.members.component.InviteMemberButton
import com.example.arrax.ui.screen.business.members.component.MemberRow
import androidx.compose.foundation.lazy.items
@Composable
fun BusinessMembersScreen(
    onBack: () -> Unit,
    onCreateInvitationClick: () -> Unit,
    modifier: Modifier = Modifier,
    onMemberClick: (String) -> Unit = {},
    onInvitationClick: (String) -> Unit = {},
    viewModel: BusinessMembersViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BusinessMembersContent(
        state = state,
        onBack = onBack,
        onCreateInvitationClick = onCreateInvitationClick,
        onMemberClick = onMemberClick,
        onInvitationClick = onInvitationClick,
        onToggleActive = viewModel::onToggleActive,
        onToggleExpired = viewModel::onToggleExpired,
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
                    InviteMemberButton(
                        onClick = onCreateInvitationClick,
                        modifier = Modifier.padding(vertical = ArxSpacing.sm)
                    )
                }
                item(key = "active") {
                    InvitationsSection(
                        title = "Invitaciones activas",
                        items = state.activeInvitations,
                        expanded = state.activeExpanded,
                        emptyText = "No hay invitaciones activas.",
                        onToggle = onToggleActive,
                        onClick = onInvitationClick
                    )
                }
                item(key = "expired") {
                    InvitationsSection(
                        title = "Invitaciones vencidas",
                        items = state.expiredInvitations,
                        expanded = state.expiredExpanded,
                        emptyText = "No hay invitaciones vencidas.",
                        onToggle = onToggleExpired,
                        onClick = onInvitationClick
                    )
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
                    else -> items(state.members, key = { it.id }) { member ->
                        MemberRow(member = member, onClick = onMemberClick)
                    }
                }
            }
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
@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun BusinessMembersPreview() {
    ArxTheme(darkTheme = true) {
        BusinessMembersContent(
            state = BusinessMembersState(
                isLoadingMembers = false,
                activeInvitations = listOf(
                    InvitationItemUi("A1", "Código + QR", "Operador", "2 de 5 personas · Expira en 3 días", true)
                ),
                members = listOf(
                    MemberUi("1", "Alex Caamal", "AC", "Propietario", isOwner = true),
                    MemberUi("2", "Carlos Ramírez", "CR", "Operador")
                )
            ),
            onBack = {}, onCreateInvitationClick = {}, onMemberClick = {},
            onInvitationClick = {}, onToggleActive = {}, onToggleExpired = {}
        )
    }
}