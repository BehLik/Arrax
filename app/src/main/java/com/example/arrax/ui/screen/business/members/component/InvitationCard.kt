package com.example.arrax.ui.screen.business.members.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.members.InvitationUi
import com.example.arrax.ui.screen.business.members.InviteTab

/** Tarjeta glass que muestra el código, el QR o un mensaje vacío según la pestaña. */
@Composable
internal fun InvitationCard(
    invitation: InvitationUi?,
    tab: InviteTab,
    modifier: Modifier = Modifier
) {
    ArxCard(modifier = modifier.fillMaxWidth(), variant = ArxCardVariant.Glass) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 96.dp)
                .padding(ArxSpacing.md),
            contentAlignment = Alignment.Center
        ) {
            when {
                invitation == null -> Text(
                    text = "Genera una invitación para tu equipo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                tab == InviteTab.Code -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
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

                else -> {
                    val qr = invitation.qrImage
                    if (qr != null) {
                        Image(
                            bitmap = qr,
                            contentDescription = "Código QR de la invitación",
                            modifier = Modifier.size(200.dp)
                        )
                    } else {
                        ArxLoading()
                    }
                }
            }
        }
    }
}