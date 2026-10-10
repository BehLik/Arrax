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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.components.feedback.ArxChipStatus
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors
import java.util.Locale

/**
 * Modelo de datos para cada ítem en el desglose del resumen.
 */
data class OrderSummaryItem(
    val id: String,
    val name: String,
    val weightKg: Double,
    val pricePerKg: Double
) {
    val totalPrice: Double get() = weightKg * pricePerKg
}

/**
 * Tarjeta de resumen de pedido con desglose de ítems, pesajes y métricas financieras.
 *
 * @param orderId Identificador único del pedido (ej. "#PED-804").
 * @param items Lista de productos/pesajes incluidos en el pedido.
 * @param discountMxn Descuento opcional aplicado en moneda local.
 * @param taxRatePercentage Porcentaje de impuestos aplicable (por defecto 0%).
 * @param statusLabel Etiqueta de estado del pedido para el encabezado.
 * @param statusType Tipo semántico del chip de estado (success, warning, info...).
 * @param modifier Modificador de diseño Compose.
 */
@Composable
fun ArxOrderSummaryCard(
    orderId: String,
    items: List<OrderSummaryItem>,
    modifier: Modifier = Modifier,
    discountMxn: Double = 0.0,
    taxRatePercentage: Double = 0.0,
    statusLabel: String = "Completado",
    statusType: ArxChipStatus = ArxChipStatus.Success
) {
    val totalWeightKg = items.sumOf { it.weightKg }
    val subtotal = items.sumOf { it.totalPrice }
    val taxAmount = subtotal * (taxRatePercentage / 100.0)
    val grandTotal = (subtotal + taxAmount - discountMxn).coerceAtLeast(0.0)

    // Variante Standard (sólida): es un ticket con muchas cifras, la legibilidad manda.
    ArxCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArxSpacing.md)
        ) {
            // --- ENCABEZADO ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Ticket de pedido",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = orderId,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                ArxStatusChip(
                    text = statusLabel,
                    status = statusType
                )
            }

            Spacer(modifier = Modifier.height(ArxSpacing.sm))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(ArxSpacing.sm))

            // --- PRODUCTOS Y PESAJES ---
            Text(
                text = "Desglose de productos",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = ArxSpacing.xs)
            )

            items.forEach { item ->
                SummaryItemRow(item = item)
                Spacer(modifier = Modifier.height(ArxSpacing.xs))
            }

            Spacer(modifier = Modifier.height(ArxSpacing.xs))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(ArxSpacing.sm))

            // --- RESUMEN DE PESO Y CANTIDAD ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .padding(horizontal = ArxSpacing.sm, vertical = ArxSpacing.xs),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${items.size} producto(s)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Peso total: ${formatDouble(totalWeightKg)} kg",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(ArxSpacing.sm))

            // --- DESGLOSE FINANCIERO ---
            FinancialRow(label = "Subtotal", amount = subtotal)

            if (discountMxn > 0.0) {
                FinancialRow(
                    label = "Descuento",
                    amount = -discountMxn,
                    isDiscount = true
                )
            }

            if (taxRatePercentage > 0.0) {
                FinancialRow(
                    label = "Impuestos (${formatDouble(taxRatePercentage)}%)",
                    amount = taxAmount
                )
            }

            Spacer(modifier = Modifier.height(ArxSpacing.xs))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(ArxSpacing.xs))

            // --- TOTAL GENERAL ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$${formatCurrency(grandTotal)} MXN",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
private fun SummaryItemRow(item: OrderSummaryItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${formatDouble(item.weightKg)} kg × $${formatCurrency(item.pricePerKg)}/kg",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = "$${formatCurrency(item.totalPrice)}",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun FinancialRow(
    label: String,
    amount: Double,
    isDiscount: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = ArxSpacing.xxs),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        // El descuento es algo bueno para el cliente: va en success, no en error.
        Text(
            text = "${if (isDiscount) "-" else ""}$${formatCurrency(kotlin.math.abs(amount))}",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDiscount) {
                MaterialTheme.extendedColors.success.color
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

private fun formatDouble(value: Double): String {
    return String.format(Locale.US, "%.2f", value)
}

private fun formatCurrency(value: Double): String {
    return String.format(Locale.US, "%,.2f", value)
}

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

@Composable
private fun ArxOrderSummaryCardPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val sampleItems = listOf(
                OrderSummaryItem("1", "Costilla", 3.5, 140.0),
                OrderSummaryItem("2", "Codillo", 2.0, 120.0),
                OrderSummaryItem("3", "Manteca", 1.2, 90.0)
            )
            Column(modifier = Modifier.padding(ArxSpacing.md)) {
                ArxOrderSummaryCard(
                    orderId = "#PED-804",
                    items = sampleItems,
                    discountMxn = 20.0,
                    statusLabel = "Listo para entrega"
                )
            }
        }
    }
}

@Preview(name = "Resumen de pedido – Light", showBackground = false)
@Composable
fun ArxOrderSummaryCardPreviewLight() = ArxOrderSummaryCardPreviewContent()

@Preview(
    name = "Resumen de pedido – Dark",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ArxOrderSummaryCardPreviewDark() = ArxOrderSummaryCardPreviewContent()