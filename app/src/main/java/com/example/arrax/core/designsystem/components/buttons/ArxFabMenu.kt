package com.example.arrax.core.designsystem.components.buttons

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing

data class ArxFabMenuItem(
    val text: String,
    val iconRes: Int,
    val onClick: () -> Unit
)

private const val ItemStaggerMs = 40
private const val ItemDurationMs = 180

/**
 * Speed-dial: FAB principal circular + acciones como mini FAB con etiqueta.
 * El estado es hoisted para que el Home pueda dibujar [ArxFabMenuScrim] detrás del menú.
 */
@Composable
fun ArxFabMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: List<ArxFabMenuItem>,
    modifier: Modifier = Modifier
) {
    BackHandler(enabled = expanded) { onExpandedChange(false) }

    val colors = MaterialTheme.colorScheme
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 45f else 0f,
        animationSpec = tween(220),
        label = "fab-rotation"
    )
    val containerColor by animateColorAsState(
        targetValue = if (expanded) colors.surface else colors.primary,
        label = "fab-container"
    )
    val contentColor by animateColorAsState(
        targetValue = if (expanded) colors.primary else colors.onPrimary,
        label = "fab-content"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
    ) {
        items.forEachIndexed { index, item ->
            // La acción más cercana al FAB entra primero.
            val delay = (items.lastIndex - index) * ItemStaggerMs
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(tween(ItemDurationMs, delay)) +
                        scaleIn(tween(ItemDurationMs, delay), initialScale = 0.8f) +
                        slideInVertically(tween(ItemDurationMs, delay)) { it / 2 },
                exit = fadeOut(tween(ItemDurationMs / 2)) +
                        scaleOut(tween(ItemDurationMs / 2), targetScale = 0.8f) +
                        slideOutVertically(tween(ItemDurationMs / 2)) { it / 2 }
            ) {
                FabMenuAction(item = item, onClick = {
                    onExpandedChange(false)
                    item.onClick()
                })
            }
        }

        FloatingActionButton(
            onClick = { onExpandedChange(!expanded) },
            shape = CircleShape,
            containerColor = containerColor,
            contentColor = contentColor,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
        ) {
            Icon(
                painter = painterResource(ArxIcons.Add),
                contentDescription = if (expanded) "Cerrar menú" else "Abrir menú",
                modifier = Modifier.rotate(rotation)
            )
        }
    }
}

@Composable
private fun FabMenuAction(item: ArxFabMenuItem, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            // Centra el mini FAB (40dp) sobre el eje del FAB principal (56dp).
            .padding(end = ArxSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
    ) {
        Surface(
            shape = CircleShape, // píldora
            color = colors.surface,
            contentColor = colors.onSurface,
            tonalElevation = ArxSpacing.sm,
            shadowElevation = ArxSpacing.xs
        ) {
            Text(
                text = item.text,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.sm)
            )
        }
        SmallFloatingActionButton(
            onClick = onClick,
            shape = CircleShape,
            containerColor = colors.secondaryContainer,
            contentColor = colors.onSecondaryContainer
        ) {
            Icon(painter = painterResource(item.iconRes), contentDescription = item.text)
        }
    }
}

/** Fondo atenuado que se muestra mientras el menú está abierto; tocarlo lo cierra. */
@Composable
fun ArxFabMenuScrim(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                )
        )
    }
}