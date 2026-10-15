
package com.example.arrax.core.designsystem.components.navigation

import android.content.res.Configuration
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxTheme

data class ArxBottomBarItem(
    val label: String,
    val iconRes: Int,
    val selectedIconRes: Int = iconRes,
    val route: String
)

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

    // Superficie flotante Soft UI.
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 12.dp,
                    shape = CircleShape,
                    ambientColor = Color.Black.copy(alpha = 0.10f),
                    spotColor = Color.Black.copy(alpha = 0.16f)
                )
                .border(
                    width = 1.dp,
                    color = colorScheme.outlineVariant.copy(alpha = 0.35f),
                    shape = CircleShape
                ),
            shape = CircleShape,
            color = colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(76.dp)
                    .padding(horizontal = 5.dp, vertical = 5.dp)
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
                        .height(66.dp)
                        .clip(CircleShape)
                        .background(colorScheme.primaryContainer)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items.forEachIndexed { index, item ->
                        val isSelected = index == selectedIndex

                        ArxBottomBarItemContent(
                            item = item,
                            isSelected = isSelected,
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

    val interactionSource = remember {
        MutableInteractionSource()
    }

    Box(
        modifier = modifier
            .height(66.dp)
            .clip(CircleShape)
            .semantics {
                selected = isSelected
            }
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
            Icon(
                painter = painterResource(
                    id = if (isSelected) {
                        item.selectedIconRes
                    } else {
                        item.iconRes
                    }
                ),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = item.label,
                color = contentColor,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) {
                    FontWeight.SemiBold
                } else {
                    FontWeight.Normal
                },
                maxLines = 1
            )
        }
    }
}

@Preview(
    showBackground = true,
    name = "Arrax Bottom Bar · Dark",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun ArxBottomBarPreview() {
    val sampleItems = listOf(
        ArxBottomBarItem(
            label = "Inicio",
            iconRes = ArxIcons.HomeOutline,
            route = "home"
        ),
        ArxBottomBarItem(
            label = "Pedidos",
            iconRes = ArxIcons.Lot,
            route = "orders"
        ),
        ArxBottomBarItem(
            label = "Cortes",
            iconRes = ArxIcons.Stock,
            route = "cuts"
        ),
        ArxBottomBarItem(
            label = "Perfil",
            iconRes = ArxIcons.User,
            route = "profile"
        )
    )

    ArxTheme(darkTheme = true) {
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
