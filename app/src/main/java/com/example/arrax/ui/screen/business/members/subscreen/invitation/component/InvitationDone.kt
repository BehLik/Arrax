package com.example.arrax.ui.screen.business.members.subscreen.invitation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.components.feedback.ArxChipStatus
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.domain.model.InviteType
import com.example.arrax.ui.screen.business.members.subscreen.invitation.CreatedInvitationUi

@Composable
internal fun InvitationDone(
    invitation: CreatedInvitationUi,
    onCopyCode: () -> Unit,
    onShare: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = ArxSpacing.md),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
    ) {
        Text(
            "Aquí tienes tu invitación. Puedes compartirla para enviarla por el medio que prefieras.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        ArxCard(modifier = Modifier.fillMaxWidth(), variant = ArxCardVariant.Glass) {
            Column(Modifier.padding(ArxSpacing.md), verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(invitation.typeLabel, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                    ArxStatusChip(text = "Activa", status = ArxChipStatus.Success)
                }
                DetailLine("Rol", invitation.roleLabel)
                DetailLine("Personas", invitation.peopleLabel)
                DetailLine("Expira", invitation.expiresLabel)

                if (invitation.type != InviteType.CODE) {
                    invitation.qrImage?.let {
                        Image(
                            bitmap = it,
                            contentDescription = "Código QR de la invitación",
                            modifier = Modifier.size(200.dp).align(Alignment.CenterHorizontally)
                        )
                    }
                }
                if (invitation.type != InviteType.QR) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("Código de acceso", style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(invitation.code, style = MaterialTheme.typography.titleLarge)
                        }
                        IconButton(onClick = onCopyCode) {
                            ArxIcon(icon = ArxIcons.List, contentDescription = "Copiar código") // placeholder hasta tener ArxIcons.Copy
                        }
                    }
                }
            }
        }

        ArxPrimaryButton(text = "Compartir", onClick = onShare)
        ArxSecondaryButton(text = "Listo", onClick = onDone) // usa tu variante secundaria/outlined
        Spacer(Modifier.height(ArxSpacing.md))
    }
}



@Composable
private fun DetailLine(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}