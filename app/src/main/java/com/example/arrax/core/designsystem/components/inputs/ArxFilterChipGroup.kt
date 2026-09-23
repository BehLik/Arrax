package com.example.arrax.core.designsystem.components.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.theme.ArxTheme

/**
 * Representa los estados de filtrado para la lista de pedidos.
 */
enum class OrderStatusFilter(val label: String) {
    ALL("Todos"),
    PENDING("Pendientes"),
    IN_WEIGHING("En Pesaje"),
    COMPLETED("Completados"),
    AUTOMATED("Automatizados")
}

/**
 * Grupo de chips de filtro horizontal para categorizar y filtrar pedidos.
 *
 * @param filters Lista de opciones de filtro a mostrar.
 * @param selectedFilter Filtro actualmente seleccionado.
 * @param onFilterSelected Callback emitido cuando el usuario selecciona un filtro.
 * @param badgeCounts Mapa opcional para mostrar contadores numéricos en cada chip (ej. Pendientes -> 4).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArxFilterChipGroup(
    filters: List<OrderStatusFilter>,
    selectedFilter: OrderStatusFilter,
    onFilterSelected: (OrderStatusFilter) -> Unit,
    modifier: Modifier = Modifier,
    badgeCounts: Map<OrderStatusFilter, Int> = emptyMap()
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters, key = { it.name }) { filter ->
            val isSelected = filter == selectedFilter
            val count = badgeCounts[filter]
            val chipText = if (count != null && count > 0) {
                "${filter.label} ($count)"
            } else {
                filter.label
            }

            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = chipText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    selectedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArxFilterChipGroupPreview() {
    ArxTheme {
        var selectedFilter by remember { mutableStateOf(OrderStatusFilter.PENDING) }
        val sampleCounts = mapOf(
            OrderStatusFilter.ALL to 12,
            OrderStatusFilter.PENDING to 5,
            OrderStatusFilter.IN_WEIGHING to 3,
            OrderStatusFilter.COMPLETED to 4
        )

        ArxFilterChipGroup(
            filters = OrderStatusFilter.entries,
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it },
            badgeCounts = sampleCounts
        )
    }
}