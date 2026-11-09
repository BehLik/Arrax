package com.example.arrax.ui.screen.business.event.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.display.ArxEmptyState
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.designsystem.components.inputs.ArxTextField
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.domain.model.CutCatalog



@Composable
internal fun CutsStep(
    cuts: List<CutCatalog>,
    catalogIsEmpty: Boolean,
    selectedIds: Set<String>,
    query: String,
    isLoading: Boolean,
    onQueryChange: (String) -> Unit,
    onToggle: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = ArxSpacing.md),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
    ) {
        StepHeader("Selecciona los cortes", "Elige los cortes que venderás en este evento.")
        ArxTextField(value = query, onValueChange = onQueryChange, label = "Buscar corte")
        Text(
            text = if (selectedIds.isEmpty()) "Puedes seleccionar varios cortes."
            else "${selectedIds.size} seleccionados",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        when {
            isLoading -> ArxLoading()
            catalogIsEmpty -> ArxEmptyState(
                title = "Sin cortes en el catálogo",
                description = "Agrega o activa cortes desde el catálogo del negocio.",
                iconRes = ArxIcons.Box
            )
            cuts.isEmpty() -> ArxEmptyState(
                title = "Sin resultados",
                description = "No hay cortes que coincidan con tu búsqueda.",
                iconRes = ArxIcons.SearchOff
            )
            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm),
                contentPadding = PaddingValues(bottom = ArxSpacing.md)
            ) {
                items(cuts, key = { it.id }) { cut ->
                    CutSelectCard(cut, selected = cut.id in selectedIds, onClick = { onToggle(cut.id) })
                }
            }
        }
    }
}