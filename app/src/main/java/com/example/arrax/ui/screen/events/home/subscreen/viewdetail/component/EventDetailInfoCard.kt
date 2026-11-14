package com.example.arrax.ui.screen.events.home.subscreen.viewdetail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.components.feedback.ArxChipStatus
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.extendedColors
import com.example.arrax.ui.screen.events.home.subscreen.viewdetail.EventViewDetailState


@Composable
internal fun EventDetailInfoCard(state: EventViewDetailState, modifier: Modifier = Modifier) {
    ArxCard(variant = ArxCardVariant.Glass, modifier = modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth().padding(ArxSpacing.md)) {
            ArxStatusChip(
                text = state.statusText,
                status = if (state.isOpen) ArxChipStatus.Success else ArxChipStatus.Neutral
            )
            Spacer(Modifier.height(ArxSpacing.xs))
            Text(state.eventName, style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(ArxSpacing.md))
            HorizontalDivider(color = MaterialTheme.extendedColors.glassBorder)
            Spacer(Modifier.height(ArxSpacing.sm))
            InfoRow(ArxIcons.Date, "Fecha", state.dateFormatted)
            InfoRow(ArxIcons.Piggy, "Cerdos", "${state.animalCount}")
            InfoRow(ArxIcons.User, "Creado por", state.createdBy)
        }
    }
}

@Composable
private fun InfoRow(icon: Int, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = ArxSpacing.xxs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArxIcon(icon = icon, size = 16.dp, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(ArxSpacing.xs))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(ArxSpacing.sm))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}