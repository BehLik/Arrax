package com.example.arrax.ui.screen.lobby.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.arrax.R
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.theme.ArxSpacing

@Composable
fun WorkspaceCard(
    onCreateBusinessClick: () -> Unit,
    onJoinClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArxCard(variant = ArxCardVariant.Glass, modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(ArxSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                Text(
                    text = stringResource(R.string.workspace_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.workspace_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)) {
                ArxPrimaryButton(
                    text = stringResource(R.string.workspace_create),
                    onClick = onCreateBusinessClick,
                    hero = true,
                    modifier = Modifier.weight(1f)
                )
                ArxSecondaryButton(
                    text = stringResource(R.string.workspace_join),
                    onClick = onJoinClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
