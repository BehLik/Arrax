package com.example.arrax.ui.screen.lobby.notification.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.extendedColors

data class NotificationUi(
    val id: String,
    val title: String,
    val message: String,
    val timeLabel: String,
    val read: Boolean
)

@Composable
fun NotificationItem(
    notification: NotificationUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .semantics { stateDescription = if (notification.read) "Leída" else "No leída" },
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.extendedColors.glass,
        border = BorderStroke(1.dp, MaterialTheme.extendedColors.glassBorder),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(ArxSpacing.md),
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
        ) {
            // Indicador de no leída; se reserva el espacio para alinear ambos estados.
            Box(
                modifier = Modifier
                    .padding(top = ArxSpacing.xs)
                    .size(ArxSpacing.xs)
                    .clip(CircleShape)
                    .background(
                        if (notification.read) MaterialTheme.colorScheme.surface.copy(alpha = 0f)
                        else MaterialTheme.colorScheme.primary
                    )
            )
            Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xxs)) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = if (notification.read) FontWeight.Normal else FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = notification.timeLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}