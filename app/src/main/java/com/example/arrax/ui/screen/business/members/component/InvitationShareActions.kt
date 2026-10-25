package com.example.arrax.ui.screen.business.members.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.buttons.ArxFieldActionButton
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing

@Composable
internal fun InvitationShareActions(
    onCopyCode: () -> Unit,
    onShareQr: () -> Unit,
    onShareLink: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
        Text("Compartir invitación", style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
            // TODO: no existe ArxIcons.Copy; agregar ic_copy (por ahora List como placeholder).
            ArxFieldActionButton(
                iconRes = ArxIcons.List,
                label = "Copiar código",
                modifier = Modifier.weight(1f),
                onClick = onCopyCode
            )
            ArxFieldActionButton(
                iconRes = ArxIcons.ScanQr,
                label = "Compartir QR",
                modifier = Modifier.weight(1f),
                onClick = onShareQr
            )
            ArxFieldActionButton(
                iconRes = ArxIcons.Link,
                label = "Compartir enlace",
                modifier = Modifier.weight(1f),
                onClick = onShareLink
            )
        }
    }
}