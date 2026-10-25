package com.example.arrax.ui.screen.business.home.component

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
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.components.display.ArxTone
import com.example.arrax.core.designsystem.components.display.color
import com.example.arrax.core.designsystem.components.feedback.ArxChipStatus
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIconBadge
import com.example.arrax.core.designsystem.icons.ArxIconStyle
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.home.HomeNextEventUi

@Composable
internal fun NextEventBanner(
    event: HomeNextEventUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val warning = ArxTone.Warning.color()
    ArxCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        variant = ArxCardVariant.Glass,
        accentColor = warning
    ) {
        Row(
            modifier = Modifier.padding(ArxSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
        ) {
            ArxIconBadge(icon = ArxIcons.Schedule, style = ArxIconStyle.Soft, accent = warning)
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
                ) {
                    Text("Evento próximo", style = MaterialTheme.typography.titleMedium)
                    ArxStatusChip(text = event.whenLabel, status = ArxChipStatus.Warning)
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