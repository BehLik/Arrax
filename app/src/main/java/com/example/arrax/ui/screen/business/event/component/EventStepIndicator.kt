package com.example.arrax.ui.screen.business.event.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.theme.ArxSpacing
private val StepLabels = listOf("Datos", "Cortes", "Precios", "Resumen", "Listo")


@Composable
internal fun StepIndicator(current: Int) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.sm),
        horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
    ) {
        StepLabels.forEachIndexed { index, label ->
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(
                    Modifier.fillMaxWidth().height(4.dp).clip(MaterialTheme.shapes.small)
                        .background(if (index <= current) colors.primary else colors.outlineVariant)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (index <= current) colors.primary else colors.onSurfaceVariant
                )
            }
        }
    }
}