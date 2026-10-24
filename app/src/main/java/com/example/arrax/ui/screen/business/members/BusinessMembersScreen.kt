package com.example.arrax.ui.screen.business.members

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors
import com.example.arrax.ui.screen.business.BusinessAvatar
import com.example.arrax.ui.screen.business.BusinessCard
import com.example.arrax.ui.screen.business.BusinessListRow
import com.example.arrax.ui.screen.business.BusinessPill
import com.example.arrax.ui.screen.business.BusinessScreenSurface
import com.example.arrax.ui.screen.business.BusinessTabs
import com.example.arrax.ui.screen.business.BusinessTone
import com.example.arrax.ui.screen.business.BusinessTopBar


@Composable
fun BusinessMembersScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onMemberClick: (memberId: String) -> Unit = {},
    onShareQr: () -> Unit = {},
    onShareLink: () -> Unit = {},
    viewModel: BusinessMembersViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val clipboard = LocalClipboardManager.current

    BusinessMembersContent(
        state = state,
        onBack = onBack,
        onMemberClick = onMemberClick,
        onInviteClick = viewModel::onInviteClick,
        onDismissInvite = viewModel::onDismissInvite,
        onInviteTabSelected = viewModel::onInviteTabSelected,
        onCopyCode = { state.invitation?.code?.let { clipboard.setText(AnnotatedString(it)) } },
        onShareQr = onShareQr,
        onShareLink = onShareLink,
        onGenerateInvitation = viewModel::onGenerateInvitation,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BusinessMembersContent(
    state: BusinessMembersState,
    onBack: () -> Unit,
    onMemberClick: (String) -> Unit,
    onInviteClick: () -> Unit,
    onDismissInvite: () -> Unit,
    onInviteTabSelected: (InviteTab) -> Unit,
    onCopyCode: () -> Unit,
    onShareQr: () -> Unit,
    onShareLink: () -> Unit,
    onGenerateInvitation: () -> Unit,
    modifier: Modifier = Modifier
) {
    BusinessScreenSurface(modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            BusinessTopBar(title = "Equipo y permisos", onBack = onBack)

            Button(
                onClick = onInviteClick,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ArxSpacing.md)
                    .heightIn(min = 48.dp)
            ) {
                ArxIcon(icon = ArxIcons.GroupAdd, size = 20.dp)
                Text(
                    text = "Crear invitación",
                    modifier = Modifier.padding(start = ArxSpacing.xs),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Text(
                text = "${state.members.size} integrantes",
                modifier = Modifier.padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.sm),
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = ArxSpacing.md, vertical = ArxSpacing.xs),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
            ) {
                items(state.members, key = { it.id }) { member ->
                    BusinessListRow(
                        title = member.name,
                        showChevron = false,
                        leading = { BusinessAvatar(initials = member.initials) },
                        trailing = {
                            BusinessPill(
                                text = member.roleLabel,
                                tone = if (member.isOwner) BusinessTone.Primary else BusinessTone.Info
                            )
                        },
                        onClick = { onMemberClick(member.id) }
                    )
                }
            }
        }

        if (state.isInviteSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = onDismissInvite,
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                InvitationSheetContent(
                    state = state,
                    onClose = onDismissInvite,
                    onTabSelected = onInviteTabSelected,
                    onCopyCode = onCopyCode,
                    onShareQr = onShareQr,
                    onShareLink = onShareLink,
                    onGenerate = onGenerateInvitation
                )
            }
        }
    }
}

@Composable
private fun InvitationSheetContent(
    state: BusinessMembersState,
    onClose: () -> Unit,
    onTabSelected: (InviteTab) -> Unit,
    onCopyCode: () -> Unit,
    onShareQr: () -> Unit,
    onShareLink: () -> Unit,
    onGenerate: () -> Unit
) {
    val invitation = state.invitation
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = ArxSpacing.md)
            .padding(bottom = ArxSpacing.xl),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Crear invitación",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineMedium
            )
            IconButton(onClick = onClose) {
                ArxIcon(icon = ArxIcons.Close, contentDescription = "Cerrar")
            }
        }

        BusinessTabs(
            labels = listOf("Código", "QR"),
            selectedIndex = InviteTab.entries.indexOf(state.inviteTab),
            onSelect = { onTabSelected(InviteTab.entries[it]) }
        )

        BusinessCard(modifier = Modifier.fillMaxWidth()) {
            if (invitation == null) {
                Box(Modifier.fillMaxWidth().heightIn(min = 96.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Genera una invitación para tu equipo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else if (state.inviteTab == InviteTab.Code) {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = invitation.code,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${invitation.expiresLabel} • ${invitation.usesLabel}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    val qr = invitation.qrImage
                    if (qr != null) {
                        Image(
                            bitmap = qr,
                            contentDescription = "Código QR de la invitación",
                            modifier = Modifier.size(200.dp)
                        )
                    } else {
                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                    }
                }
            }
        }

        if (invitation != null) {
            Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                Text("Rol asignado", style = MaterialTheme.typography.labelLarge)
                BusinessListRow(
                    title = invitation.roleLabel,
                    showChevron = false,
                    leading = { ArxIcon(icon = ArxIcons.User, tint = MaterialTheme.colorScheme.primary) }
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                Text("Compartir invitación", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                    // TODO: no existe ArxIcons.Copy; ic_copy pendiente (se usa List como placeholder).
                    ShareTile("Copiar código", ArxIcons.List, onCopyCode, Modifier.weight(1f))
                    ShareTile("Compartir QR", ArxIcons.ScanQr, onShareQr, Modifier.weight(1f))
                    ShareTile("Compartir enlace", ArxIcons.Link, onShareLink, Modifier.weight(1f))
                }
            }
        }

        Button(
            onClick = onGenerate,
            enabled = !state.isGenerating,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
        ) {
            if (state.isGenerating) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Generar nueva invitación", style = MaterialTheme.typography.labelLarge)
            }
        }

        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
        ) {
            ArxIcon(icon = ArxIcons.Info, tint = MaterialTheme.colorScheme.onSurfaceVariant, size = 16.dp)
            Text(
                text = "La invitación expira automáticamente en la fecha indicada o al alcanzar el límite de usos.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ShareTile(
    label: String,
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val glass = MaterialTheme.extendedColors
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = glass.glass,
        border = BorderStroke(1.dp, glass.glassBorder)
    ) {
        Column(
            modifier = Modifier
                .clickable(role = Role.Button, onClick = onClick)
                .heightIn(min = 72.dp)
                .padding(ArxSpacing.xs),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ArxIcon(icon = icon, tint = MaterialTheme.colorScheme.primary)
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Preview(showBackground = true, widthDp = 360, heightDp = 780, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BusinessMembersPreview() {
    ArxTheme(darkTheme =true) {
        BusinessMembersContent(
            state = BusinessMembersState(
                members = listOf(
                    MemberUi("1", "Alex Caamal", "AC", "Owner", isOwner = true),
                    MemberUi("2", "Carlos Ramírez", "CR", "Operador"),
                    MemberUi("3", "María López", "ML", "Operador")
                ),
                isInviteSheetVisible = false
            ),
            onBack = {}, onMemberClick = {}, onInviteClick = {}, onDismissInvite = {},
            onInviteTabSelected = {}, onCopyCode = {}, onShareQr = {}, onShareLink = {},
            onGenerateInvitation = {}
        )
    }
}