package com.example.arrax.ui.screen.business.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.BusinessListRow
import com.example.arrax.ui.screen.business.BusinessPill
import com.example.arrax.ui.screen.business.BusinessSectionHeader
import com.example.arrax.ui.screen.business.BusinessToneBadge
import com.example.arrax.ui.screen.business.home.HomeAlertUi

@Composable
internal fun HomeAlertsSection(
    alerts: List<HomeAlertUi>,
    onAlertsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
    ) {
        BusinessSectionHeader(
            title = "Alertas y recordatorios",
            actionLabel = "Ver todas",
            onAction = onAlertsClick
        )
        alerts.forEach { alert ->
            BusinessListRow(
                title = alert.title,
                subtitle = alert.detail,
                leading = { BusinessToneBadge(icon = alert.icon, tone = alert.tone) },
                trailing = { BusinessPill(text = alert.statusLabel, tone = alert.tone) },
                onClick = onAlertsClick
            )
        }
    }
}