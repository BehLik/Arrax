package com.example.arrax.core.designsystem.components.inputs

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

/**
 * Control segmentado genérico. Sirve para "Por corte / Por cliente" y para los modos de
 * activación ("Botón / Comando de voz / Híbrido"). La opción activa se distingue por
 * fondo + borde + selección semántica, no solo por color.
 */
@Composable
fun ArxSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val outerShape = MaterialTheme.shapes.medium
    val innerShape = MaterialTheme.shapes.small

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(outerShape)
            .background(MaterialTheme.extendedColors.glass)
            .border(1.dp, MaterialTheme.extendedColors.glassBorder, outerShape)
            .padding(ArxSpacing.xxs),
        horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xxs)
    ) {
        options.forEachIndexed { index, option ->
            val selected = index == selectedIndex
            val segmentColor by animateColorAsState(
                targetValue = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                label = "segment_color"
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp)
                    .clip(innerShape)
                    .background(segmentColor)
                    .then(
                        if (selected) {
                            Modifier.border(1.dp, MaterialTheme.colorScheme.primary, innerShape)
                        } else {
                            Modifier
                        }
                    )
                    .selectable(
                        selected = selected,
                        enabled = enabled,
                        role = Role.RadioButton,
                        onClick = { onOptionSelected(index) }
                    )
                    .padding(horizontal = ArxSpacing.xs),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.labelLarge,
                    color = when {
                        !enabled -> MaterialTheme.extendedColors.disabled
                        selected -> MaterialTheme.colorScheme.onPrimaryContainer
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ArxSegmentedControlPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            var mode by remember { mutableIntStateOf(0) }
            var activation by remember { mutableIntStateOf(2) }
            Column(
                modifier = Modifier.padding(ArxSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
            ) {
                ArxSegmentedControl(
                    options = listOf("Por corte", "Por cliente"),
                    selectedIndex = mode,
                    onOptionSelected = { mode = it }
                )
                ArxSegmentedControl(
                    options = listOf("Botón", "Comando de voz", "Híbrido"),
                    selectedIndex = activation,
                    onOptionSelected = { activation = it }
                )
            }
        }
    }
}

@Preview(name = "Segmentado – Light", showBackground = false)
@Composable
fun ArxSegmentedControlPreviewLight() = ArxSegmentedControlPreviewContent()

@Preview(name = "Segmentado – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxSegmentedControlPreviewDark() = ArxSegmentedControlPreviewContent()