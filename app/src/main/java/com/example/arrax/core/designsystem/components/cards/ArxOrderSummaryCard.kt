package com.example.arrax.core.designsystem.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.theme.ArxTheme
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
 * @param modifier Modificador de diseño Compose.
 */
@Composable
fun ArxOrderSummaryCard(
    orderId: String,
    items: List<OrderSummaryItem>,
    modifier: Modifier = Modifier,
    discountMxn: Double = 0.0,
    taxRatePercentage: Double = 0.0,
    statusLabel: String = "Completado"
) {
    val totalWeightKg = items.sumOf { it.weightKg }
    val subtotal = items.sumOf { it.totalPrice }
    val taxAmount = subtotal * (taxRatePercentage / 100.0)
    val grandTotal = (subtotal + taxAmount - discountMxn).coerceAtLeast(0.0)

    ArxCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // --- ENCABEZADO DE LA CARD ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Ticket de Pedido",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = orderId,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                ArxStatusChip(
                    text = statusLabel,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(12.dp))

            // --- LISTA DE PRODUCTOS Y PESAJES ---
            Text(
                text = "Desglose de Productos",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            items.forEach { item ->
                SummaryItemRow(item = item)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(12.dp))

            // --- RESUMEN DE PESO Y CANTIDAD ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${items.size} producto(s)",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Peso Total: ${formatDouble(totalWeightKg)} kg",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

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

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(8.dp))

            // --- TOTAL GENERAL ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TOTAL",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$${formatCurrency(grandTotal)} MXN",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
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
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
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
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
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
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "${if (isDiscount) "-" else ""}$${formatCurrency(kotlin.math.abs(amount))}",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isDiscount) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (isDiscount) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun formatDouble(value: Double): String {
    return String.format(Locale.US, "%.2f", value)
}

private fun formatCurrency(value: Double): String {
    return String.format(Locale.US, "%,.2f", value)
}

// --- PREVIEW ---

@Preview(showBackground = true)
@Composable
fun ArxOrderSummaryCardPreview() {
    ArxTheme {
        val sampleItems = listOf(
            OrderSummaryItem("1", "Pechuga Deshuesada", 15.4, 110.0),
            OrderSummaryItem("2", "Muslo y Pierna", 22.0, 75.5),
            OrderSummaryItem("3", "Alitas Marinadas", 8.2, 95.0)
        )

        Column(modifier = Modifier.padding(16.dp)) {
            ArxOrderSummaryCard(
                orderId = "#PED-804",
                items = sampleItems,
                discountMxn = 0.0,
                statusLabel = "Listo para Entrega"
            )
        }
    }
}