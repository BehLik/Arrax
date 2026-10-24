package com.example.arrax.ui.screen.business.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.BusinessCard
import com.example.arrax.ui.screen.business.BusinessPill
import com.example.arrax.ui.screen.business.BusinessTone
import com.example.arrax.ui.screen.business.BusinessToneBadge
import com.example.arrax.ui.screen.business.home.HomeNextEventUi

/** Banner teñido con el tono Warning; por eso usa BusinessCard (ArxCard no admite tono). */
@Composable
internal fun NextEventBanner(
    event: HomeNextEventUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BusinessCard(
        modifier = modifier.fillMaxWidth(),
        tone = BusinessTone.Warning,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
        ) {
            BusinessToneBadge(icon = ArxIcons.Schedule, tone = BusinessTone.Warning)
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
                ) {
                    Text("Evento próximo", style = MaterialTheme.typography.titleMedium)
                    BusinessPill(text = event.whenLabel, tone = BusinessTone.Warning)
                }
                Text(
                    text = event.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            ArxIcon(icon = ArxIcons.Forward, size = 20.dp)
        }
    }
}