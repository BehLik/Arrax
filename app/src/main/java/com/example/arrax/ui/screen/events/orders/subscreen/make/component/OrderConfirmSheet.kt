package com.example.arrax.ui.screen.events.orders.subscreen.make.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.cards.ArxOrderSummaryCard
import com.example.arrax.core.designsystem.components.cards.OrderSummaryItem
import com.example.arrax.core.designsystem.components.feedback.ArxChipStatus
import com.example.arrax.domain.model.RequestMode
import com.example.arrax.ui.screen.events.orders.subscreen.make.OrderLineUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OrderConfirmSheet(
    customerName: String,
    notes: String,
    lines: List<OrderLineUi>,
    isSaving: Boolean,
    onConfirm: () -> Unit,
    onConfirmAndAddAnother: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 16.dp).padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ArxOrderSummaryCard(
                orderId = customerName,
                items = lines.map { it.toSummaryItem() },
                statusLabel = "Por confirmar",
                statusType = ArxChipStatus.Warning,
            )
            if (notes.isNotBlank()) Text("Nota: $notes", style = MaterialTheme.typography.bodyMedium)
            ArxPrimaryButton(
                text = if (isSaving) "Guardando…" else "Guardar pedido",
                onClick = onConfirm, enabled = !isSaving,
                modifier = Modifier.fillMaxWidth().height(56.dp),
            )
            OutlinedButton(onClick = onConfirmAndAddAnother, enabled = !isSaving, modifier = Modifier.fillMaxWidth()) {
                Text("Guardar y hacer otro pedido para este cliente")
            }
        }
    }
}

private fun OrderLineUi.toSummaryItem() = OrderSummaryItem(
    id = cutId,
    name = cutName,
    // En modo pesos uso monto/precio sin redondear, para que el ticket muestre exactamente $200.00
    weightKg = if (mode == RequestMode.MONEY && pricePerKgCents > 0)
        amountCents.toDouble() / pricePerKgCents else targetGrams / 1000.0,
    pricePerKg = pricePerKgCents / 100.0,
)