
package com.example.arrax.ui.screen.lobby.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.arrax.R
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.images.ArxImages
import com.example.arrax.core.designsystem.theme.ArxSpacing

@Composable
internal fun WorkspaceCard(
    onCreateBusinessClick: () -> Unit,
    onJoinClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArxCard(
        variant = ArxCardVariant.Glass,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {

            // Imagen panorámica decorativa del paisaje
            Image(
                painter = painterResource(ArxImages.Meadow),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.CenterEnd,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxWidth(0.58f)
                    .height(180.dp)
                    .alpha(0.78f)
            )

            // Fundido horizontal para conservar la legibilidad
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            colorStops = arrayOf(
                                0.0f to MaterialTheme.colorScheme.surface,
                                0.30f to MaterialTheme.colorScheme.surface,
                                0.55f to MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                                0.80f to MaterialTheme.colorScheme.surface.copy(alpha = 0.35f),
                                1.0f to Color.Transparent
                            )
                        )
                    )
            )

            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ArxSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
                ) {
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

                Row(
                    horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
                ) {
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
}
