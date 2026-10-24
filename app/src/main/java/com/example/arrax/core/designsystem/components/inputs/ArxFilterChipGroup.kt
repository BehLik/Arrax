package com.example.arrax.core.designsystem.components.inputs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

/**
 * Representa los estados de filtrado para la lista de pedidos.
 */
enum class OrderStatusFilter(val label: String) {
    ALL("Todos"),
    PENDING("Pendientes"),
    IN_WEIGHING("En pesaje"),
    COMPLETED("Completados"),
    AUTOMATED("Automatizados")
}

/**
 * Grupo de chips de filtro horizontal. Normal = glass sutil; seleccionado = primaryContainer + borde primary.
 *
 * @param filters Opciones de filtro a mostrar.
 * @param selectedFilter Filtro seleccionado.
 * @param onFilterSelected Callback al seleccionar un filtro.
 * @param badgeCounts Contadores opcionales por chip (ej. Pendientes -> 4).
 */
/** Versión genérica: sirve para cualquier lista de opciones. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ArxFilterChipGroup(
    filters: List<T>,
    selected: T,
    onSelected: (T) -> Unit,
    label: (T) -> String,
    modifier: Modifier = Modifier,
    badgeCounts: Map<T, Int> = emptyMap()
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = ArxSpacing.md, vertical = ArxSpacing.xs),
        horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
    ) {
        itemsIndexed(filters, key = { index, _ -> index }) { _, filter ->
            val isSelected = filter == selected
            val count = badgeCounts[filter]
            val chipText = if (count != null && count > 0) "${label(filter)} ($count)" else label(filter)

            FilterChip(
                selected = isSelected,
                onClick = { onSelected(filter) },
                shape = MaterialTheme.shapes.small,
                label = { Text(text = chipText, style = MaterialTheme.typography.labelLarge) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.extendedColors.glass,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = MaterialTheme.extendedColors.glassBorder,
                    selectedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

/** API original de pedidos: se conserva y delega en la genérica. del desino orinalll*/
@Composable
fun ArxFilterChipGroup(
    filters: List<OrderStatusFilter>,
    selectedFilter: OrderStatusFilter,
    onFilterSelected: (OrderStatusFilter) -> Unit,
    modifier: Modifier = Modifier,
    badgeCounts: Map<OrderStatusFilter, Int> = emptyMap()
) {
    ArxFilterChipGroup(
        filters = filters,
        selected = selectedFilter,
        onSelected = onFilterSelected,
        label = { it.label },
        modifier = modifier,
        badgeCounts = badgeCounts
    )
}

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

@Composable
private fun ArxFilterChipGroupPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
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
}

@Preview(name = "Filtros – Light", showBackground = false)
@Composable
fun ArxFilterChipGroupPreviewLight() = ArxFilterChipGroupPreviewContent()

@Preview(
    name = "Filtros – Dark",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ArxFilterChipGroupPreviewDark() = ArxFilterChipGroupPreviewContent()