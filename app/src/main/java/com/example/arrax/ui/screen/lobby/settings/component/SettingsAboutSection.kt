package com.example.arrax.ui.screen.lobby.settings.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.brand.ArxLogo
import com.example.arrax.core.designsystem.components.brand.ArxLogoLayout
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.extendedColors

@Composable
fun SettingsAboutSection(
    version: String,
    onLinkClick: (AboutLink) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsGroupCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
        ) {
            ArxLogo(layout = ArxLogoLayout.Stacked, markSize = 80.dp)
            Text(
                text = version,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        AboutLink.entries.forEach { link ->
            HorizontalDivider(color = MaterialTheme.extendedColors.glassBorder)
            Text(
                text = stringResource(link.titleRes),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLinkClick(link) }
                    .defaultMinSize(minHeight = 48.dp)
                    .padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.sm)
            )
        }
    }
}