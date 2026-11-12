package com.example.arrax.ui.screen.events.home.subscreen.viewdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.arrax.core.common.Money
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.event.component.CutImage
import com.example.arrax.ui.screen.events.home.subscreen.viewdetail.EventCutDetailUi


@Composable
internal fun EventDetailCutRow(cut: EventCutDetailUi, modifier: Modifier = Modifier) {
    ArxCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(ArxSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)
        ) {
            if (cut.catalogCut != null) {
                CutImage(cut.catalogCut, Modifier.size(56.dp).clip(MaterialTheme.shapes.small))
            } else {
                Spacer(Modifier.size(56.dp))
            }
            Column(Modifier.weight(1f)) {
                Text(cut.name, style = MaterialTheme.typography.titleSmall)
                Text(
                    "Precio por kg",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                Money.format(cut.pricePerKgCents),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}