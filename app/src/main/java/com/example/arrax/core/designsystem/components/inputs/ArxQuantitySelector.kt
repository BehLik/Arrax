package com.example.arrax.core.designsystem.components.inputs

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

/** Selector de cantidad (− valor +). Botones de 48dp para cumplir el touch target mínimo. */
@Composable
fun ArxQuantitySelector(
    value: Double,
    onValueChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
    step: Double = 0.5,
    unit: String = "kg",
    minValue: Double = 0.0
) {
    val shape = MaterialTheme.shapes.small

    Row(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.extendedColors.glass)
            .border(1.dp, MaterialTheme.extendedColors.glassBorder, shape)
            .padding(ArxSpacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { if (value - step >= minValue) onValueChange(value - step) },
            enabled = value > minValue,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.extendedColors.disabled
            ),
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(id = ArxIcons.Remove),
                contentDescription = "Disminuir",
                modifier = Modifier.size(20.dp)
            )
        }

        Text(
            text = "$value $unit",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .widthIn(min = 72.dp)
                .padding(horizontal = ArxSpacing.xs)
        )

        IconButton(
            onClick = { onValueChange(value + step) },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(id = ArxIcons.Add),
                contentDescription = "Aumentar",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

@Composable
private fun ArxQuantitySelectorPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ArxQuantitySelector(
                value = 2.5,
                onValueChange = {},
                unit = "kg",
                modifier = Modifier.padding(ArxSpacing.md)
            )
        }
    }
}

@Preview(name = "Cantidad – Light", showBackground = false)
@Composable
fun ArxQuantitySelectorPreviewLight() = ArxQuantitySelectorPreviewContent()

@Preview(
    name = "Cantidad – Dark",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ArxQuantitySelectorPreviewDark() = ArxQuantitySelectorPreviewContent()