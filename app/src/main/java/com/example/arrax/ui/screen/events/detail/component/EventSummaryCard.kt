package com.example.arrax.ui.screen.events.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.arrax.R
import com.example.arrax.core.common.Money
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.domain.model.EventStatus
import com.example.arrax.domain.model.EventWithCuts
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EventSummaryCard(eventWithCuts: EventWithCuts) {
    val event = eventWithCuts.event
    val dateLabel = remember(event.date) {
        SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("es-MX")).format(Date(event.date))
    }
    val isOpen = event.status == EventStatus.OPEN

    ArxCard {
        Column(modifier = Modifier.fillMaxWidth().padding(ArxSpacing.md)) {
            Text(event.name)
            ArxStatusChip(
                text = stringResource(if (isOpen) R.string.event_status_open else R.string.event_status_closed),
                containerColor = if (isOpen) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (isOpen) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(stringResource(R.string.event_date_label, dateLabel))
            Text(stringResource(R.string.event_pigs_label, event.pigCount))

            Text(stringResource(R.string.event_cuts_title))
            eventWithCuts.cuts.forEach { cut ->
                Text(stringResource(R.string.event_cut_line, cut.name, Money.format(cut.pricePerKgCents)))
            }
        }
    }
}
