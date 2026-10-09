package com.example.arrax.core.designsystem.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.theme.ArxSpacing

/**
 * Tarjeta de métrica. Normal = glass sutil; [isHighlighted] = primaryContainer (métrica importante).
 */
@Composable
fun ArxMetricCard(
    title: String,
    value: String,
    iconRes: Int,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    isHighlighted: Boolean = false
) {
    val titleColor = if (isHighlighted) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val valueColor = if (isHighlighted) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    ArxCard(
        modifier = modifier,
        variant = if (isHighlighted) ArxCardVariant.Standard else ArxCardVariant.Glass,
        colors = if (isHighlighted) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        } else {
            null
        }
    ) {
        Column(modifier = Modifier.padding(ArxSpacing.md)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = titleColor
                )
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null, // decorativo: el título ya lo describe
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(ArxSpacing.sm))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge,
                color = valueColor
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(ArxSpacing.xxs))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = titleColor
                )
            }
        }
    }
}