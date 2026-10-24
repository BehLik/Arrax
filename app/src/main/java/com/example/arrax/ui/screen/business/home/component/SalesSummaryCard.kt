package com.example.arrax.ui.screen.business.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.BusinessDot
import com.example.arrax.ui.screen.business.home.HomeSalesSliceUi

@Composable
internal fun SalesSummaryCard(
    total: String,
    slices: List<HomeSalesSliceUi>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
    ) {
        Text("Resumen de ventas (últimos 7 días)", style = MaterialTheme.typography.titleMedium)
        ArxCard(modifier = Modifier.fillMaxWidth(), variant = ArxCardVariant.Glass) {
            Row(
                modifier = Modifier.padding(ArxSpacing.md),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    ArxIcon(icon = ArxIcons.TrendingUp, tint = MaterialTheme.colorScheme.primary)
                    Text(total, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        text = "Total en ventas",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                SalesDonut(slices = slices)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(ArxSpacing.xxs)
                ) {
                    slices.forEach { slice ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
                        ) {
                            BusinessDot(tone = slice.tone)
                            Text(
                                text = slice.label,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1
                            )
                            Text(slice.percentLabel, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}