
package com.example.arrax.ui.screen.events.home.subscreen.edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
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
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Los FocusRequesters se conservan mientras los IDs no cambien.
    val cutIds = cuts.map { it.cutId }

    val focusRequesters = remember(cutIds) {
        cutIds.map { FocusRequester() }
    }

    fun finishEditing() {
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(ArxSpacing.md),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
    ) {
        item(key = "header") {
            StepHeader(
                "Cortes del evento",
                "Edita precios, quita cortes o agrega nuevos. Los cortes nuevos necesitan precio."
            )
        }

        items(
            items = cuts,
            key = { it.cutId }
        ) { cut ->

            val index = cutIds.indexOf(cut.cutId)
            val isLast = index == cuts.lastIndex

            EventEditCutRow(
                cut = cut,
                catalogCut = catalog.firstOrNull {
                    it.id == cut.cutId
                },
                focusRequester = focusRequesters[index],
                isLast = isLast,
                onPriceChange = {
                    onPriceChange(cut.cutId, it)
                },
                onNext = {
                    val nextIndex = index + 1

                    if (nextIndex <= cuts.lastIndex) {
                        coroutineScope.launch {
                            // El índice 0 corresponde al encabezado.
                            listState.animateScrollToItem(nextIndex + 1)

                            focusRequesters[nextIndex].requestFocus()
                        }
                    }
                },
                onDone = {
                    finishEditing()
                },
                onRemove = {
                    if (index >= 0) {
                        focusManager.clearFocus()
                        onRemoveCut(cut.cutId)
                    }
                }
            )
        }

        item(key = "add_cuts") {
            ArxSecondaryButton(
                text = "Agregar cortes",
                onClick = {
                    finishEditing()
                    onAddCutsClick()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
