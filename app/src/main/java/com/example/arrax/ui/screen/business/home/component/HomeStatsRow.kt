package com.example.arrax.ui.screen.business.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.cards.ArxMetricCard
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing

@Composable
internal fun HomeStatsRow(
    upcomingEvents: Int,
    customers: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
    ) {
        ArxMetricCard(
            title = "Próximos eventos",
            value = upcomingEvents.toString(),
            subtitle = "Este mes",
            iconRes = ArxIcons.Event,
            modifier = Modifier.weight(1f)
        )
        ArxMetricCard(
            title = "Clientes",
            value = customers.toString(),
            subtitle = "Registrados",
            iconRes = ArxIcons.GroupAdd,
            modifier = Modifier.weight(1f)
        )
    }
}