package com.example.arrax.core.designsystem.components.navigation

import android.content.res.Configuration
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.feedback.ArxBadge
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

// Medidas del componente (no son colores ni espaciado de layout).
private val BarHeight = 76.dp
private val ItemHeight = 66.dp
private val BarInnerPadding = 5.dp

/**
 * Elemento de la barra. [badgeCount] es opcional: con valor > 0 muestra una insignia
 * sobre el icono (ej. pedidos pendientes).
 */
data class ArxBottomBarItem(
    val label: String,
    val iconRes: Int,
    val selectedIconRes: Int = iconRes,
    val route: String,
    val badgeCount: Int = 0
)

/**
 * Barra de navegación flotante: píldora sólida con borde sutil, sombra mínima y un
 * indicador primaryContainer que se desliza entre rutas (sin rebote).
 * Cada ítem mide 66dp de alto: cumple el touch target de 48dp.
 */
@Composable
fun ArxBottomBar(
    items: List<ArxBottomBarItem>,
    currentRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return

    val selectedIndex = items.indexOfFirst { it.route == currentRoute }
        .coerceAtLeast(0)

    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = ArxSpacing.lg, vertical = ArxSpacing.sm),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape,
            color = colorScheme.surface,
            shadowElevation = 2.dp,
            border = BorderStroke(1.dp, MaterialTheme.extendedColors.glassBorder)
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(BarHeight)
                    .padding(BarInnerPadding)
            ) {
                val itemWidth = maxWidth / items.size

                // Indicador que se desplaza entre las rutas.
                val indicatorOffset: Dp by animateDpAsState(
                    targetValue = itemWidth * selectedIndex,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow,
                        dampingRatio = Spring.DampingRatioNoBouncy
                    ),
                    label = "ArxSelectionOffset"
                )

                Box(
                    modifier = Modifier
                        .offset(x = indicatorOffset)
                        .width(itemWidth)
                        .height(ItemHeight)
                        .clip(CircleShape)
                        .background(colorScheme.primaryContainer)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items.forEachIndexed { index, item ->
                        ArxBottomBarItemContent(
                            item = item,
                            isSelected = index == selectedIndex,
                            modifier = Modifier.weight(1f),
                            onClick = { onItemClick(item.route) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ArxBottomBarItemContent(
    item: ArxBottomBarItem,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    val contentColor = if (isSelected) {
        colorScheme.onPrimaryContainer
    } else {
        colorScheme.onSurfaceVariant
    }

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .height(ItemHeight)
            .clip(CircleShape)
            .semantics { selected = isSelected }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Tab,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box {
                Icon(
                    painter = painterResource(
                        id = if (isSelected) item.selectedIconRes else item.iconRes
                    ),
                    contentDescription = null, // la etiqueta de abajo ya lo describe
                    tint = contentColor,
                    modifier = Modifier.size(22.dp)
                )
                if (item.badgeCount > 0) {
                    ArxBadge(
                        count = item.badgeCount,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 10.dp, y = (-8).dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(ArxSpacing.xxs))

            // El ítem activo se distingue por el indicador y el color, no por el peso del texto.
            Text(
                text = item.label,
                color = contentColor,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

@Composable
private fun ArxBottomBarPreviewContent(darkTheme: Boolean) {
    val sampleItems = listOf(
        ArxBottomBarItem(
            label = "Inicio",
            iconRes = ArxIcons.HomeOutline,
            selectedIconRes = ArxIcons.HomeFilled,
            route = "home"
        ),
        ArxBottomBarItem(
            label = "Pedidos",
            iconRes = ArxIcons.Order,
            route = "orders",
            badgeCount = 5
        ),
        ArxBottomBarItem(
            label = "Manos libres",
            iconRes = ArxIcons.Mic,
            route = "handsfree"
        ),
        ArxBottomBarItem(
            label = "Clientes",
            iconRes = ArxIcons.TeamOutline,
            selectedIconRes = ArxIcons.TeamFilled,
            route = "customers"
        ),
        ArxBottomBarItem(
            label = "Más",
            iconRes = ArxIcons.More,
            route = "more"
        )
    )

    ArxTheme(darkTheme = darkTheme) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            ArxBottomBar(
                items = sampleItems,
                currentRoute = "orders",
                onItemClick = {}
            )
        }
    }
}

@Preview(name = "Bottom Bar – Light", showBackground = false)
@Composable
fun ArxBottomBarPreviewLight() = ArxBottomBarPreviewContent(darkTheme = false)

@Preview(
    name = "Bottom Bar – Dark",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ArxBottomBarPreviewDark() = ArxBottomBarPreviewContent(darkTheme = true)