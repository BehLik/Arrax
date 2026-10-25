package com.example.arrax.ui.screen.business.alerts.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.inputs.ArxFilterChipGroup
import com.example.arrax.ui.screen.business.alerts.AlertsTab

@Composable
internal fun AlertsTabs(
    selected: AlertsTab,
    onSelected: (AlertsTab) -> Unit,
    modifier: Modifier = Modifier
) {
    ArxFilterChipGroup(
        filters = AlertsTab.entries,
        selected = selected,
        onSelected = onSelected,
        label = { it.label() },
        modifier = modifier
    )
}

internal fun AlertsTab.label(): String = when (this) {
    AlertsTab.All -> "Todas"
    AlertsTab.Events -> "Eventos"
    AlertsTab.Invitations -> "Invitaciones"
    AlertsTab.Orders -> "Pedidos"
}