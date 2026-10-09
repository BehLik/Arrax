package com.example.arrax.core.designsystem.components.cards

import android.content.res.Configuration
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.components.voice.ArxVoiceStatusIndicator
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

/**
 * Ítem de pedido en fila. Normal = glass sutil; si es el siguiente en la cola de voz
 * se resalta con primaryContainer.
 */
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
    val titleColor = if (isCurrentInVoiceQueue) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val secondaryColor = if (isCurrentInVoiceQueue) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    ArxCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        variant = if (isCurrentInVoiceQueue) ArxCardVariant.Standard else ArxCardVariant.Glass,
        colors = if (isCurrentInVoiceQueue) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        } else {
            null
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArxSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = productName,
                        style = MaterialTheme.typography.titleMedium,
                        color = titleColor
                    )
                    if (isCurrentInVoiceQueue) {
                        Spacer(modifier = Modifier.width(ArxSpacing.xs))
                        ArxVoiceStatusIndicator(isListening = true)
                    }
                }

                Spacer(modifier = Modifier.height(ArxSpacing.xxs))

                Text(
                    text = "Requerido: $targetWeight",
                    style = MaterialTheme.typography.bodyMedium,
                    color = secondaryColor
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                if (isWeighed && actualWeight != null) {
                    Text(
                        text = actualWeight,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(ArxSpacing.xxs))
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

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

@Composable
private fun ArxOrderItemPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(ArxSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
            ) {
                // Siguiente en la cola de voz
                ArxOrderItemHorizontalCard(
                    productName = "Costilla",
                    targetWeight = "1.5 kg",
                    actualWeight = null,
                    isCurrentInVoiceQueue = true,
                    isWeighed = false,
                    onClick = {}
                )
                // Ya pesado
                ArxOrderItemHorizontalCard(
                    productName = "Codillo",
                    targetWeight = "2.0 kg",
                    actualWeight = "2.02 kg",
                    isCurrentInVoiceQueue = false,
                    isWeighed = true,
                    onClick = {}
                )
                // Pendiente
                ArxOrderItemHorizontalCard(
                    productName = "Espinazo",
                    targetWeight = "1.0 kg",
                    actualWeight = null,
                    isCurrentInVoiceQueue = false,
                    isWeighed = false,
                    onClick = {}
                )
            }
        }
    }
}

@Preview(name = "Ítem de pedido – Light", showBackground = false)
@Composable
fun ArxOrderItemHorizontalCardPreviewLight() = ArxOrderItemPreviewContent()

@Preview(
    name = "Ítem de pedido – Dark",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ArxOrderItemHorizontalCardPreviewDark() = ArxOrderItemPreviewContent()