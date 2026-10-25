package com.example.arrax.ui.screen.business.customers.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.display.ArxEmptyState
import com.example.arrax.core.designsystem.icons.ArxIcons

@Composable
internal fun CustomersEmptyState(
    isSearching: Boolean,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        if (isSearching) {
            ArxEmptyState(
                title = "Sin resultados",
                description = "Intenta buscar con otro nombre o teléfono.",
                iconRes = ArxIcons.SearchOff
            )
        } else {
            ArxEmptyState(
                title = "Aún no hay clientes",
                description = "Registra a tu primer cliente para empezar a tomar pedidos.",
                iconRes = ArxIcons.User,
                actionText = "Agregar cliente",
                onActionClick = onAddClick
            )
        }
    }
}