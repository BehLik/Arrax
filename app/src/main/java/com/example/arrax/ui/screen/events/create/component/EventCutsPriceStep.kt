package com.example.arrax.ui.screen.events.create.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
//import com.example.arrax.core.designsystem.components.buttons.ArxButton
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.inputs.ArxTextField
import com.example.arrax.domain.model.CutCatalog

// FIX: es keyboardOptions = KeyboardOptions(...), no keyboardType suelto
// (patron estandar de Compose - no confirmado 1:1 contra tu ArxTextField real,
// pero es casi con certeza asi si envuelve un TextField/OutlinedTextField).
@Composable
fun EventCutsPriceStep(
    selectedCuts: List<CutCatalog>,
    pricesByCutId: Map<String, String>,
    canConfirm: Boolean,
    onPriceChange: (cutId: String, rawValue: String) -> Unit,
    onConfirm: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        LazyColumn(
            modifier = Modifier.weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(selectedCuts, key = { it.id }) { cut ->
                ArxTextField(
                    value = pricesByCutId[cut.id].orEmpty(),
                    onValueChange = { onPriceChange(cut.id, it) },
                    label = "${cut.name} - precio por kg",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }

        ArxPrimaryButton(
            text = "Confirmar evento",
            enabled = canConfirm,
            onClick = onConfirm
        )
    }
}
