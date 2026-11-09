package com.example.arrax.ui.screen.business.event.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.inputs.ArxTextField
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.domain.model.CutCatalog
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.text.input.ImeAction

@Composable
internal fun PricesStep(
    cuts: List<CutCatalog>,
    prices: Map<String, PriceInput>,
    onPriceChange: (String, String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(ArxSpacing.md),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
    ) {
        item {
            StepHeader("Asignar precios", "Ingresa el precio por kg para cada corte seleccionado.")
        }
        items(cuts, key = { it.id }) { cut ->
            ArxCard {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(ArxSpacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)
                ) {
                    CutImage(cut, Modifier.size(56.dp).clip(MaterialTheme.shapes.small))
                    Column(Modifier.weight(1f)) {
                        Text(cut.name, style = MaterialTheme.typography.titleSmall)
                        ArxTextField(
                            value = prices[cut.id]?.rawText.orEmpty(),
                            onValueChange = { onPriceChange(cut.id, it) },
                            label = "Precio por kg ($)",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                },
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            )
                        )
                    }
                }
            }
        }
        item {
            Text(
                "El precio se usará para calcular pedidos en kg o $.",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}