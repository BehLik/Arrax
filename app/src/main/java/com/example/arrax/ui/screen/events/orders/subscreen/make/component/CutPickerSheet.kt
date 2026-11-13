package com.example.arrax.ui.screen.events.orders.subscreen.make.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.arrax.core.common.toMoney
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.domain.model.EventCut

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CutPickerSheet(
    cuts: List<EventCut>,
    onPick: (EventCut) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Agregar corte", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (cuts.isEmpty()) {
                Text("Ya agregaste todos los cortes del evento.")
            } else {
                cuts.forEach { cut ->
                    ArxCard(onClick = { onPick(cut) }, modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(16.dp).heightIn(min = 32.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(cut.name, style = MaterialTheme.typography.titleMedium)
                            Text("${cut.pricePerKgCents.toLong().toMoney()}/kg")
                        }
                    }
                }
            }
        }
    }
}