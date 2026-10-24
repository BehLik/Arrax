package com.example.arrax.ui.screen.business.alerts.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.ui.screen.business.BusinessDot
import com.example.arrax.ui.screen.business.BusinessListRow
import com.example.arrax.ui.screen.business.BusinessToneBadge
import com.example.arrax.ui.screen.business.alerts.AlertUi

@Composable
internal fun AlertRow(
    alert: AlertUi,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BusinessListRow(
        title = alert.title,
        modifier = modifier,
        subtitle = alert.detail,
        detail = alert.supporting,
        tone = alert.tone.takeIf { alert.isUnread },
        leading = { BusinessToneBadge(icon = alert.icon, tone = alert.tone) },
        trailing = { if (alert.isUnread) BusinessDot(tone = alert.tone) },
        onClick = { onClick(alert.id) }
    )
}