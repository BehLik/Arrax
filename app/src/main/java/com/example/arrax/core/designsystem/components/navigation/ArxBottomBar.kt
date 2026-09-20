package com.example.arrax.core.designsystem.components.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 8.dp // Da un sutil efecto de elevación
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClick(item.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = if (isSelected) item.selectedIconRes else item.iconRes),
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Preview(showBackground = true, name = "Barra de Navegación Inferior")
@Composable
fun ArxBottomBarPreview() {
    // Estado simulado para la previsualización
    var currentRoute by remember { mutableIntStateOf(0) }

    val sampleItems = listOf(
        ArxBottomBarItem(
            label = "Inicio",
            iconRes = ArxIcons.HomeOutline, // Asegúrate de tener estos iconos en ArxIcons
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

    ArxTheme {
        ArxBottomBar(
            items = sampleItems,
            currentRoute = sampleItems[currentRoute].route,
            onItemClick = { route ->
                currentRoute = sampleItems.indexOfFirst { it.route == route }
            }
        )
    }
}