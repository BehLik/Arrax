package com.example.arrax.ui.screen.events.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.domain.model.EventStatus
import com.example.arrax.domain.model.EventWithCuts
import java.text.SimpleDateFormat
import java.util.Locale

// FIX: ArxStatusChip pide text/containerColor/contentColor (confirmado por el
// compilador), no label.
@Composable
fun EventSummaryCard(eventWithCuts: EventWithCuts) {
    val event = eventWithCuts.event
    val dateLabel = SimpleDateFormat("dd/MM/yyyy", Locale("es", "MX")).format(event.date)
    val isOpen = event.status == EventStatus.OPEN

    ArxCard {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(event.name)
            ArxStatusChip(
                text = if (isOpen) "Abierto" else "Cerrado",
                containerColor = if (isOpen) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (isOpen) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text("Fecha: $dateLabel")
            Text("Cerdos: ${event.pigCount}")

            Text("Cortes:")
            eventWithCuts.cuts.forEach { cut ->
                Text("- ${cut.name} - $${cut.pricePerKg} / kg")
            }
        }
    }
}
