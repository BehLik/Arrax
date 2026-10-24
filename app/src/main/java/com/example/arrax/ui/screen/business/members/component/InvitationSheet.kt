package com.example.arrax.ui.screen.business.members.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.BusinessListRow
import com.example.arrax.ui.screen.business.BusinessTabs
import com.example.arrax.ui.screen.business.members.BusinessMembersState
import com.example.arrax.ui.screen.business.members.InviteTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InvitationSheet(
    state: BusinessMembersState,
    onDismiss: () -> Unit,
    onTabSelected: (InviteTab) -> Unit,
    onCopyCode: () -> Unit,
    onShareQr: () -> Unit,
    onShareLink: () -> Unit,
    onGenerate: () -> Unit
) {
    val invitation = state.invitation

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
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
                IconButton(onClick = onDismiss) {
                    ArxIcon(icon = ArxIcons.Close, contentDescription = "Cerrar")
                }
            }

            BusinessTabs(
                labels = listOf("Código", "QR"),
                selectedIndex = InviteTab.entries.indexOf(state.inviteTab),
                onSelect = { onTabSelected(InviteTab.entries[it]) }
            )

            InvitationCard(invitation = invitation, tab = state.inviteTab)

            if (invitation != null) {
                Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                    Text("Rol asignado", style = MaterialTheme.typography.labelLarge)
                    BusinessListRow(
                        title = invitation.roleLabel,
                        showChevron = false,
                        leading = {
                            ArxIcon(icon = ArxIcons.User, tint = MaterialTheme.colorScheme.primary)
                        }
                    )
                }

                InvitationShareActions(
                    onCopyCode = onCopyCode,
                    onShareQr = onShareQr,
                    onShareLink = onShareLink
                )
            }

            ArxPrimaryButton(
                text = "Generar nueva invitación",
                onClick = onGenerate,
                loading = state.isGenerating
            )

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
            ) {
                ArxIcon(
                    icon = ArxIcons.Info,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    size = 16.dp
                )
                Text(
                    text = "La invitación expira automáticamente en la fecha indicada o al alcanzar el límite de usos.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}