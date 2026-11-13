package com.example.arrax.ui.screen.business.event.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.common.toMoney
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.event.BusinessEventCreateState


@Composable
internal fun SummaryStep(state: BusinessEventCreateState) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(ArxSpacing.md),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
    ) {
        StepHeader("Resumen", "Revisa que todo esté correcto antes de crear el evento.")

        ArxCard {
            Column(Modifier.fillMaxWidth().padding(ArxSpacing.md), verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)) {
                Text("Evento", style = MaterialTheme.typography.titleSmall)
                SummaryRow(ArxIcons.Event, "Nombre", state.name.trim())
                SummaryRow(ArxIcons.Date, "Fecha", state.dateMillis?.let(::formatDate).orEmpty())
                SummaryRow(ArxIcons.Box, "Animales", animalsLabel(state.animalCount))
            }
        }

        ArxCard {
            Column(Modifier.fillMaxWidth().padding(ArxSpacing.md), verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)) {
                Text("Precios por kg", style = MaterialTheme.typography.titleSmall)
                state.selectedCuts.forEach { cut ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(cut.name, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "${(state.priceCents(cut.id) ?: 0L).toMoney()}/kg",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}