package com.example.arrax.ui.screen.business.event.subscreen.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.components.feedback.ArxChipStatus
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.icons.ArxIconBadge
import com.example.arrax.core.designsystem.icons.ArxIconStyle
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.domain.model.Event
import com.example.arrax.domain.model.EventStatus
import com.example.arrax.ui.screen.business.event.component.animalsLabel
import com.example.arrax.ui.screen.business.event.component.formatDate

@Composable
internal fun EventListCard(event: Event, onClick: () -> Unit) {
    val isOpen = event.status == EventStatus.OPEN
    ArxCard(onClick = onClick, variant = ArxCardVariant.Glass) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(ArxSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
        ) {
            ArxIconBadge(icon = ArxIcons.Event, style = ArxIconStyle.Soft)
            Column(Modifier.weight(1f)) {
                Text(event.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${formatDate(event.date)} • ${animalsLabel(event.animalCount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            ArxStatusChip(
                text = if (isOpen) "Abierto" else "Cerrado",
                status = if (isOpen) ArxChipStatus.Success else ArxChipStatus.Neutral // ajusta a tus valores
            )
        }
    }
}