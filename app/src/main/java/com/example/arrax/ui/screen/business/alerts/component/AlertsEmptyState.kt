package com.example.arrax.ui.screen.business.alerts.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.display.ArxEmptyState
import com.example.arrax.core.designsystem.icons.ArxIcons

@Composable
internal fun AlertsEmptyState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        ArxEmptyState(
            title = "No hay alertas por ahora",
            description = "Cuando haya eventos, invitaciones o pedidos que atender, aparecerán aquí.",
            iconRes = ArxIcons.Notification
        )
    }
}