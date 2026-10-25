package com.example.arrax.ui.screen.business.members.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.display.ArxListItem
import com.example.arrax.core.designsystem.components.feedback.ArxBottomSheet
import com.example.arrax.core.designsystem.components.inputs.ArxFilterChipGroup
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.members.BusinessMembersState
import com.example.arrax.ui.screen.business.members.InviteTab

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

    ArxBottomSheet(
        onDismissRequest = onDismiss,
        title = "Crear invitación",
        icon = ArxIcons.GroupAdd,
        showCloseButton = true
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)) {
            ArxFilterChipGroup(
                filters = InviteTab.entries,
                selected = state.inviteTab,
                onSelected = onTabSelected,
                label = { if (it == InviteTab.Code) "Código" else "QR" }
            )

            InvitationCard(invitation = invitation, tab = state.inviteTab)

            if (invitation != null) {
                Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                    Text("Rol asignado", style = MaterialTheme.typography.labelLarge)
                    ArxListItem(
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