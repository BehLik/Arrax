package com.example.arrax.core.designsystem.components.display

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

/** Cierre del flujo: check grande, resumen del ciclo y acciones para continuar. */
@Composable
fun ArxQueueCompletedSummary(
    processedOrders: Int,
    totalWeightKg: Double,
    totalAmount: Double,
    onStartNewCycle: () -> Unit,
    onViewSummary: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Todos los pedidos completados"
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(ArxSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ArxSuccessCheck(size = 96.dp, haptic = true)

        Spacer(modifier = Modifier.height(ArxSpacing.xl))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(ArxSpacing.xl))

        ArxCard(modifier = Modifier.fillMaxWidth(), variant = ArxCardVariant.Glass) {
            Column(modifier = Modifier.padding(ArxSpacing.md)) {
                SummaryLine(label = "Pedidos procesados", value = processedOrders.toString())
                Spacer(modifier = Modifier.height(ArxSpacing.xs))
                SummaryLine(label = "Peso total", value = arxFormatWeight(totalWeightKg))
                Spacer(modifier = Modifier.height(ArxSpacing.sm))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(ArxSpacing.sm))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Importe total",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${arxFormatMoney(totalAmount)} MXN",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(ArxSpacing.xl))

        // Un solo CTA hero por pantalla.
        ArxPrimaryButton(text = "Iniciar nuevo ciclo", onClick = onStartNewCycle, hero = true)
        Spacer(modifier = Modifier.height(ArxSpacing.sm))
        ArxSecondaryButton(text = "Ver resumen", onClick = onViewSummary)
    }
}

@Composable
private fun SummaryLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ArxQueueCompletedSummaryPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ArxQueueCompletedSummary(
                processedOrders = 5,
                totalWeightKg = 8.21,
                totalAmount = 876.3,
                onStartNewCycle = {},
                onViewSummary = {}
            )
        }
    }
}

@Preview(name = "Cola completada – Light", showBackground = false)
@Composable
fun ArxQueueCompletedSummaryPreviewLight() = ArxQueueCompletedSummaryPreviewContent()

@Preview(name = "Cola completada – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxQueueCompletedSummaryPreviewDark() = ArxQueueCompletedSummaryPreviewContent()