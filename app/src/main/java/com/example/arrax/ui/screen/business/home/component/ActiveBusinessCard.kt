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
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIconBadge
import com.example.arrax.core.designsystem.icons.ArxIconStyle
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing

@Composable
internal fun ActiveBusinessCard(
    businessName: String,
    roleLabel: String,
    memberCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArxCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        variant = ArxCardVariant.Glass
    ) {
        Row(
            modifier = Modifier.padding(ArxSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)
        ) {
            ArxIconBadge(
                icon = ArxIcons.Storage,
                style = ArxIconStyle.Solid,
                containerSize = 56.dp,
                iconSize = 28.dp
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "NEGOCIO ACTIVO",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(businessName, style = MaterialTheme.typography.headlineMedium)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xxs)
                ) {
                    ArxIcon(icon = ArxIcons.User, tint = MaterialTheme.colorScheme.primary, size = 16.dp)
                    Text(
                        text = "$roleLabel • $memberCount integrantes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            ArxIcon(icon = ArxIcons.Forward, size = 20.dp)
        }
    }
}