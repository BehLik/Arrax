package com.example.arrax.ui.screen.business.alerts.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.display.ArxListItem
import com.example.arrax.core.designsystem.components.display.color
import com.example.arrax.core.designsystem.components.feedback.ArxBadgeDot
import com.example.arrax.core.designsystem.icons.ArxIconBadge
import com.example.arrax.core.designsystem.icons.ArxIconStyle
import com.example.arrax.ui.screen.business.alerts.AlertUi

@Composable
internal fun AlertRow(
    alert: AlertUi,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val toneColor = alert.tone.color()
    ArxListItem(
        title = alert.title,
        modifier = modifier,
        subtitle = alert.detail,
        detail = alert.supporting,
        accentColor = toneColor.takeIf { alert.isUnread },
        leading = { ArxIconBadge(icon = alert.icon, style = ArxIconStyle.Soft, accent = toneColor) },
        trailing = { if (alert.isUnread) ArxBadgeDot(containerColor = toneColor) },
        onClick = { onClick(alert.id) }
    )
}