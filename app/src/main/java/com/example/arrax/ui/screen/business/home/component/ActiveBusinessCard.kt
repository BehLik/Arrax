
package com.example.arrax.ui.screen.business.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.images.ArxImages
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
        Box(modifier = Modifier.fillMaxWidth()) {

            // Imagen panorámica de la granja
            Image(
                painter = painterResource(id = ArxImages.Farm),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.CenterEnd,
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0.78f)
            )

            // Degradado progresivo para proteger la información
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            colorStops = arrayOf(
                                0.0f to MaterialTheme.colorScheme.surface,
                                0.35f to MaterialTheme.colorScheme.surface,
                                0.65f to MaterialTheme.colorScheme.surface.copy(alpha = 0.80f),
                                1.0f to Color.Transparent
                            )
                        )
                    )
            )

            // Contenido de la tarjeta
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ArxSpacing.md),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
                ) {
                    Text(
                        text = "NEGOCIO ACTIVO",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = businessName,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xxs)
                    ) {
                        ArxIcon(
                            icon = ArxIcons.User,
                            tint = MaterialTheme.colorScheme.primary,
                            size = 16.dp
                        )

                        Text(
                            text = "$roleLabel • $memberCount integrantes",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }

                ArxIcon(
                    icon = ArxIcons.Forward,
                    size = 20.dp,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
