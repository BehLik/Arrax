package com.example.arrax.ui.screen.business.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.display.ArxListItem
import com.example.arrax.core.designsystem.components.display.ArxSectionHeader
import com.example.arrax.core.designsystem.components.display.color
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.components.feedback.toChipStatus
import com.example.arrax.core.designsystem.icons.ArxIconBadge
import com.example.arrax.core.designsystem.icons.ArxIconStyle
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.home.HomeAlertUi

@Composable
internal fun HomeAlertsSection(
    alerts: List<HomeAlertUi>,
    onAlertsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
        ArxSectionHeader(
            title = "Alertas y recordatorios",
            actionLabel = "Ver todas",
            onAction = onAlertsClick
        )
        alerts.forEach { alert ->
            ArxListItem(
                title = alert.title,
                subtitle = alert.detail,
                leading = {
                    ArxIconBadge(icon = alert.icon, style = ArxIconStyle.Soft, accent = alert.tone.color())
                },
                trailing = { ArxStatusChip(text = alert.statusLabel, status = alert.tone.toChipStatus()) },
                onClick = onAlertsClick
            )
        }
    }
}