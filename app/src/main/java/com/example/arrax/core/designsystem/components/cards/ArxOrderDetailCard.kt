package com.example.arrax.core.designsystem.components.cards

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.display.ArxAvatar
import com.example.arrax.core.designsystem.components.display.ArxOrderStage
import com.example.arrax.core.designsystem.components.display.ArxOrderStatusTimeline
import com.example.arrax.core.designsystem.components.display.arxFormatMoney
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

/**
 * Vista expandida de un pedido (expanded_order_detail): cliente, resumen, instrucciones y
 * cronología de etapas. Pasa [onStageClick] solo si el usuario tiene permiso para actualizar etapas.
 */
@Composable
fun ArxOrderDetailCard(
    order: ArxOrderListItem,
    modifier: Modifier = Modifier,
    instructions: String? = null,
    stageTimes: Map<ArxOrderStage, String> = emptyMap(),
    visibleStages: List<ArxOrderStage> = ArxOrderStage.entries,
    onStageClick: ((ArxOrderStage) -> Unit)? = null
) {
    ArxCard(modifier = modifier.fillMaxWidth(), variant = ArxCardVariant.Glass) {
        Column(modifier = Modifier.padding(ArxSpacing.md)) {
            // --- Cliente ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                ArxAvatar(initials = order.initials, size = 48.dp)
                Spacer(modifier = Modifier.width(ArxSpacing.sm))
                Column {
                    Text(
                        text = order.customer,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = order.time,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(ArxSpacing.sm))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(ArxSpacing.sm))

            // --- Resumen del pedido ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "Pedido",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${arxFormatQuantity(order.quantity)} ${order.unit} · ${order.cut}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = arxFormatMoney(order.totalMxn),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.End
                    )
                }
            }

            // --- Instrucciones del cliente ---
            if (!instructions.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(ArxSpacing.md))
                Text(
                    text = "Instrucciones del cliente",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(ArxSpacing.xxs))
                Text(
                    text = instructions,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(ArxSpacing.sm)
                )
            }

            Spacer(modifier = Modifier.height(ArxSpacing.md))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(ArxSpacing.sm))

            // --- Cronología ---
            Text(
                text = "Seguimiento",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = ArxSpacing.xs)
            )
            ArxOrderStatusTimeline(
                stages = order.stages,
                visibleStages = visibleStages,
                stageTimes = stageTimes,
                onStageClick = onStageClick
            )
        }
    }
}

@Composable
private fun ArxOrderDetailCardPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ArxOrderDetailCard(
                modifier = Modifier.padding(ArxSpacing.md),
                order = ArxOrderListItem(
                    id = "order_001",
                    customer = "Diana López",
                    cut = "Morcilla",
                    quantity = 1.0,
                    unit = "kg",
                    time = "10:24 a. m.",
                    totalMxn = 130.0,
                    stages = setOf(ArxOrderStage.Registered, ArxOrderStage.Weighed, ArxOrderStage.Delivered)
                ),
                instructions = "Sin grasa por favor, y separar 1 kg para asar.",
                stageTimes = mapOf(
                    ArxOrderStage.Registered to "10:24 a. m.",
                    ArxOrderStage.Weighed to "10:31 a. m.",
                    ArxOrderStage.Delivered to "10:40 a. m.",

                ),
                onStageClick = {}
            )
        }
    }
}

@Preview(name = "Detalle de pedido – Light", showBackground = false)
@Composable
fun ArxOrderDetailCardPreviewLight() = ArxOrderDetailCardPreviewContent()

@Preview(name = "Detalle de pedido – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxOrderDetailCardPreviewDark() = ArxOrderDetailCardPreviewContent()