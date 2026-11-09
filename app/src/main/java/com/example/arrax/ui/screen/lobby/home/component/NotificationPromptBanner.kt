package com.example.arrax.ui.screen.lobby.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.arrax.R
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.extendedColors

@Composable
internal fun NotificationPromptBanner(
    onEnable: () -> Unit,
    onLater: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.extendedColors.glassBorder)
    ) {
        Column(Modifier.padding(ArxSpacing.md), verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
            Text(stringResource(R.string.lobby_notifications_prompt_title), style = MaterialTheme.typography.titleSmall)
            Text(
                stringResource(R.string.lobby_notifications_prompt_body),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onLater) { Text(stringResource(R.string.lobby_notifications_prompt_later)) }
                TextButton(onClick = onEnable) { Text(stringResource(R.string.lobby_notifications_prompt_enable)) }
            }
        }
    }
}