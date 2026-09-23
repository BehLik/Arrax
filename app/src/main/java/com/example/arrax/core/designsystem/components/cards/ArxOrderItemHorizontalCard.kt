package com.example.arrax.core.designsystem.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.components.voice.ArxVoiceStatusIndicator
import com.example.arrax.core.designsystem.theme.ArxTheme

@Composable
fun ArxOrderItemHorizontalCard(
    productName: String,
    targetWeight: String,
    actualWeight: String?,
    isCurrentInVoiceQueue: Boolean,
    isWeighed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isCurrentInVoiceQueue) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    ArxCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = productName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (isCurrentInVoiceQueue) {
                        Spacer(modifier = Modifier.width(8.dp))
                        ArxVoiceStatusIndicator(isListening = true)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Requerido: $targetWeight",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                if (isWeighed && actualWeight != null) {
                    Text(
                        text = actualWeight,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    ArxStatusChip(
                        text = "Pesado",
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else {
                    ArxStatusChip(
                        text = "Pendiente",
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArxOrderItemHorizontalCardPreview() {
    ArxTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Item activo en cola de voz
            ArxOrderItemHorizontalCard(
                productName = "Pechuga Deshuesada",
                targetWeight = "15.0 kg",
                actualWeight = null,
                isCurrentInVoiceQueue = true,
                isWeighed = false,
                onClick = {}
            )

            // Item ya completado
            ArxOrderItemHorizontalCard(
                productName = "Muslo y Pierna",
                targetWeight = "20.0 kg",
                actualWeight = "20.2 kg",
                isCurrentInVoiceQueue = false,
                isWeighed = true,
                onClick = {}
            )
        }
    }
}