package com.example.arrax.ui.screen.business.more.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.theme.ArxSpacing

@Composable
internal fun MoreSection(
    title: String,
    options: List<MoreOption>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ArxCard(modifier = Modifier.fillMaxWidth(), variant = ArxCardVariant.Glass) {
            options.forEachIndexed { index, option ->
                MoreOptionRow(option = option)
                if (index < options.lastIndex) {
                    HorizontalDivider(
                        // Alinea el divisor con el texto: padding + badge (40dp) + separación.
                        modifier = Modifier.padding(start = ArxSpacing.md + 40.dp + ArxSpacing.sm),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}