package com.example.arrax.ui.screen.business.alerts.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing

@Composable
internal fun MarkAllReadButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArxSecondaryButton(
        text = "Marcar todas como leídas",
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.sm),
        leadingIcon = { ArxIcon(icon = ArxIcons.DoneAll, size = 20.dp) }
    )
}