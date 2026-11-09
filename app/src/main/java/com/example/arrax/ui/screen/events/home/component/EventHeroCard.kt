package com.example.arrax.ui.screen.events.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.components.feedback.ArxChipStatus
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.images.ArxImages
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.extendedColors
import com.example.arrax.ui.screen.events.home.EventHomeState

@Composable
internal fun EventHeroCard(
    state: EventHomeState,
    onEditNameClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArxCard(variant = ArxCardVariant.Glass, modifier = modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Capa decorativa derecha con fundido hacia la izquierda
            Image(
                painter = painterResource(id = ArxImages.CutMeat),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxWidth(0.55f)
                    .height(170.dp)
                    .alpha(0.85f)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxWidth(0.55f)
                    .height(170.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(modifier = Modifier.fillMaxWidth().padding(ArxSpacing.md)) {
                // El punto verde lo dibuja ArxStatusChip solo (status != Neutral)
                ArxStatusChip(text = state.statusText, status = ArxChipStatus.Success)

                Spacer(Modifier.height(ArxSpacing.xs))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = state.eventName,
                        style = MaterialTheme.typography.headlineLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(Modifier.width(ArxSpacing.xs))
                    Surface(
                        onClick = onEditNameClick,
                        shape = CircleShape,
                        color = MaterialTheme.extendedColors.glass,
                        border = BorderStroke(1.dp, MaterialTheme.extendedColors.glassBorder),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            ArxIcon(
                                icon = ArxIcons.Edit,
                                tint = MaterialTheme.colorScheme.onSurface,
                                size = 16.dp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(ArxSpacing.xs))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    ArxIcon(
                        icon = ArxIcons.Date,
                        size = 16.dp,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(ArxSpacing.xxs))
                    Text(
                        text = state.dateFormatted,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(ArxSpacing.sm))
                    VerticalDivider(
                        modifier = Modifier.height(14.dp),
                        color = MaterialTheme.extendedColors.glassBorder
                    )
                    Spacer(Modifier.width(ArxSpacing.sm))
                    ArxIcon(
                        icon = ArxIcons.Piggy,
                        size = 16.dp,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(ArxSpacing.xxs))
                    Text(
                        text = "${state.pigsCount} cerdos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(ArxSpacing.md))
                HorizontalDivider(color = MaterialTheme.extendedColors.glassBorder)
                Spacer(Modifier.height(ArxSpacing.sm))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ArxIcon(
                            icon = ArxIcons.Inventory,
                            size = 16.dp,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(ArxSpacing.xxs))
                        Text(
                            text = "Cortes configurados: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${state.configuredCutsCount}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ArxIcon(
                            icon = ArxIcons.User,
                            size = 16.dp,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.width(ArxSpacing.xxs))
                        Text(
                            text = "Creado por: ${state.createdBy}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}