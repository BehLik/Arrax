package com.example.arrax.ui.screen.business.alerts.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.alerts.AlertUi

@Composable
internal fun AlertsList(
    alerts: List<AlertUi>,
    onAlertClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(ArxSpacing.md),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
    ) {
        items(alerts, key = { it.id }) { alert ->
            AlertRow(alert = alert, onClick = onAlertClick)
        }
    }
}