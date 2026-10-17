package com.example.arrax.ui.screen.events.create.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.arrax.R
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.inputs.ArxTextField
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.domain.model.CutCatalog
import com.example.arrax.ui.screen.events.create.PriceInput

@Composable
fun EventCutsPriceStep(
    selectedCuts: List<CutCatalog>,
    pricesByCutId: Map<String, PriceInput>,
    canConfirm: Boolean,
    onPriceChange: (cutId: String, rawValue: String) -> Unit,
    onConfirm: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(ArxSpacing.md)) {
        LazyColumn(
            modifier = Modifier.weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
        ) {
            items(selectedCuts, key = { it.id }) { cut ->
                val price = pricesByCutId[cut.id]
                ArxTextField(
                    value = price?.rawText.orEmpty(),
                    onValueChange = { onPriceChange(cut.id, it) },
                    label = stringResource(R.string.event_cut_price_label, cut.name),
                    // Decimal: Number hides the decimal separator on some keyboards.
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    // If ArxTextField exposes an error param, wire it here: price?.isError == true
                )
            }
        }

        ArxPrimaryButton(
            text = stringResource(R.string.event_confirm),
            enabled = canConfirm,
            onClick = onConfirm
        )
    }
}
