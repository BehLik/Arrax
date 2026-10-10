package com.example.arrax.core.designsystem.components.display

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import java.util.Locale

// Formatos compartidos por las tarjetas de pedido (peso en kg, dinero en MXN con 2 decimales).
internal fun arxFormatWeight(kg: Double): String = String.format(Locale.US, "%.2f kg", kg)

internal fun arxFormatMoney(value: Double): String = String.format(Locale.US, "\$%,.2f", value)

/**
 * Panel de peso, precio/kg y total. Los números cambian con una animación de 300 ms.
 * Si no hay peso ([weightKg] == null) muestra "—": el sistema nunca inventa un peso.
 */
@Composable
fun ArxWeightDisplay(
    weightKg: Double?,
    pricePerKg: Double?,
    total: Double?,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    accentColor: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        DisplayColumn(
            label = "Peso",
            value = weightKg,
            format = ::arxFormatWeight,
            valueStyle = MaterialTheme.typography.headlineMedium,
            color = contentColor,
            labelColor = contentColor
        )
        DisplayColumn(
            label = "Precio/kg",
            value = pricePerKg,
            format = ::arxFormatMoney,
            valueStyle = MaterialTheme.typography.titleMedium,
            color = contentColor,
            labelColor = contentColor
        )
        DisplayColumn(
            label = "Total",
            value = total,
            format = ::arxFormatMoney,
            valueStyle = MaterialTheme.typography.headlineMedium,
            color = accentColor,
            labelColor = contentColor,
            horizontalAlignment = Alignment.End
        )
    }
}

@Composable
private fun DisplayColumn(
    label: String,
    value: Double?,
    format: (Double) -> String,
    valueStyle: TextStyle,
    color: Color,
    labelColor: Color,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(horizontalAlignment = horizontalAlignment) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = labelColor
        )
        AnimatedNumberText(value = value, format = format, style = valueStyle, color = color)
    }
}

@Composable
private fun AnimatedNumberText(
    value: Double?,
    format: (Double) -> String,
    style: TextStyle,
    color: Color
) {
    if (value == null) {
        Text(text = "—", style = style, color = color)
        return
    }
    val animated by animateFloatAsState(
        targetValue = value.toFloat(),
        animationSpec = tween(300),
        label = "weight_number"
    )
    Text(text = format(animated.toDouble()), style = style, color = color)
}

@Composable
private fun ArxWeightDisplayPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(ArxSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.xl)
            ) {
                ArxWeightDisplay(weightKg = 2.48, pricePerKg = 100.0, total = 248.0)
                ArxWeightDisplay(weightKg = null, pricePerKg = 120.0, total = null)
            }
        }
    }
}

@Preview(name = "Peso y total – Light", showBackground = false)
@Composable
fun ArxWeightDisplayPreviewLight() = ArxWeightDisplayPreviewContent()

@Preview(name = "Peso y total – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxWeightDisplayPreviewDark() = ArxWeightDisplayPreviewContent()