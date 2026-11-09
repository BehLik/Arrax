package com.example.arrax.ui.screen.business.event.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.icons.ArxIconBadge
import com.example.arrax.core.designsystem.icons.ArxIconShape
import com.example.arrax.core.designsystem.icons.ArxIconStyle
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.event.BusinessEventCreateState


@Composable
internal fun DoneStep(state: BusinessEventCreateState) {
    Column(
        modifier = Modifier.fillMaxSize().padding(ArxSpacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.md, Alignment.CenterVertically)
    ) {
        ArxIconBadge(
            icon = ArxIcons.Check,
            style = ArxIconStyle.Solid,
            shape = ArxIconShape.Circle,
            containerSize = 96.dp,
            iconSize = 48.dp
        )
        Text("¡Evento creado!", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Tu evento está listo para comenzar a recibir pedidos.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        ArxCard {
            Column(Modifier.fillMaxWidth().padding(ArxSpacing.md)) {
                Text(state.name.trim(), style = MaterialTheme.typography.titleMedium)
                Text(
                    "${state.dateMillis?.let(::formatDate).orEmpty()} • ${animalsLabel(state.animalCount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}