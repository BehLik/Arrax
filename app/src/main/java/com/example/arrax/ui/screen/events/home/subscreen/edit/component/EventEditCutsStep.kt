package com.example.arrax.ui.screen.events.home.subscreen.edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.domain.model.CutCatalog
import com.example.arrax.ui.screen.business.event.component.StepHeader
import com.example.arrax.ui.screen.events.home.subscreen.edit.EditableCut


@Composable
internal fun EventEditCutsStep(
    cuts: List<EditableCut>,
    catalog: List<CutCatalog>,
    onPriceChange: (String, String) -> Unit,
    onRemoveCut: (String) -> Unit,
    onAddCutsClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(ArxSpacing.md),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
    ) {
        item {
            StepHeader(
                "Cortes del evento",
                "Edita precios, quita cortes o agrega nuevos. Los cortes nuevos necesitan precio."
            )
        }
        items(cuts, key = { it.cutId }) { cut ->
            EventEditCutRow(
                cut = cut,
                catalogCut = catalog.firstOrNull { it.id == cut.cutId },
                onPriceChange = { onPriceChange(cut.cutId, it) },
                onRemove = { onRemoveCut(cut.cutId) }
            )
        }
        item {
            ArxSecondaryButton(
                text = "Agregar cortes",
                onClick = onAddCutsClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
