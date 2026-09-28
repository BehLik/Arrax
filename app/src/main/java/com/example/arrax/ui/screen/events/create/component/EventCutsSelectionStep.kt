package com.example.arrax.ui.screen.events.create.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
//import com.example.arrax.core.designsystem.components.buttons.ArxButton
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.domain.model.CutCatalog

// SUPUESTO: ArxCard(onClick, content) admite click; si tu ArxCard real es solo
// contenedor sin onClick, mueve el Checkbox.onCheckedChange como unico trigger.
@Composable
fun EventCutsSelectionStep(
    cuts: List<CutCatalog>,
    selectedCutIds: Set<String>,
    isLoading: Boolean,
    canContinue: Boolean,
    onToggleCut: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        if (isLoading) {
            ArxLoading()
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cuts, key = { it.id }) { cut ->
                    ArxCard(onClick = { onToggleCut(cut.id) }) {
                        Row(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                            Checkbox(
                                checked = cut.id in selectedCutIds,
                                onCheckedChange = { onToggleCut(cut.id) }
                            )
                            Text(cut.name)
                        }
                    }
                }
            }
        }

        ArxPrimaryButton(
            text = "Continuar",
            enabled = canContinue,
            onClick = onContinue
        )
    }
}