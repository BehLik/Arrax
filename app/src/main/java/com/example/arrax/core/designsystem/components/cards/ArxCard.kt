package com.example.arrax.core.designsystem.components.cards

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.icons.ArxIcons
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
@Composable
fun ArxCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ),
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    // Si la tarjeta tiene una acción de clic, usamos la variante interactiva de M3
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = MaterialTheme.shapes.medium, // Toma el 16.dp de tu ArxShapes
            colors = colors,
            elevation = elevation,
            content = content
        )
    } else {
        // Si no es interactiva, usamos la variante estática
        Card(
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            colors = colors,
            elevation = elevation,
            content = content
        )
    }
}


@Composable
fun ArxGlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    // Dejamos que el componente decida internamente según el tema si no se le pasa un color explícito
    baseColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    content: @Composable ColumnScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val shape = MaterialTheme.shapes.medium

    // 1. Ajuste de colores dinámico
    // En modo oscuro, surfaceVariant (0xFF3F4945) da un mejor tono de cristal que el surface (muy negro).
    val actualBaseColor = if (baseColor != Color.Unspecified) baseColor else {
        if (isDark) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
    }

    val actualBorderColor = if (borderColor != Color.Unspecified) borderColor else {
        MaterialTheme.colorScheme.outlineVariant
    }

    // 2. Ajuste de opacidades (Alpha)
    // En dark mode usamos menos alpha (0.2f) para no blanquear la tarjeta, manteniendo el cristal sutil.
    // En light mode usamos más alpha (0.45f) para que el blanco esmerilado se note.
    val backgroundAlpha = if (isDark) 0.2f else 0.45f
    val borderAlpha = if (isDark) 0.15f else 0.3f

    val cardColors = CardDefaults.cardColors(
        containerColor = actualBaseColor.copy(alpha = backgroundAlpha),
        contentColor = MaterialTheme.colorScheme.onSurface
    )

    val cardElevation = CardDefaults.cardElevation(defaultElevation = 0.dp)

    val baseModifier = modifier
        .border(
            width = 1.dp,
            color = actualBorderColor.copy(alpha = borderAlpha),
            shape = shape
        )

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = baseModifier,
            shape = shape,
            colors = cardColors,
            elevation = cardElevation,
            content = content
        )
    } else {
        Card(
            modifier = baseModifier,
            shape = shape,
            colors = cardColors,
            elevation = cardElevation,
            content = content
        )
    }
}




@Preview(showBackground = true, name = "Familia de Cards Arrax")
@Composable
fun ArxCardsPreview() {
    ArxTheme() {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ejemplo de métricas lado a lado..
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ArxMetricCard(
                    title = "Animales",
                    value = "1,450",
                    subtitle = "+5% este mes",
                    iconRes = ArxIcons.Camera, // Reemplaza con tus íconos reales
                    modifier = Modifier.weight(1f),
                    isHighlighted = true
                )
                ArxMetricCard(
                    title = "Bajas",
                    value = "12",
                    iconRes = ArxIcons.NfcOutline, // Reemplaza con tus íconos reales
                    modifier = Modifier.weight(1f),

                )
            }

            // Ejemplo de tarjeta de acción...
            ArxActionCard(
                title = "Registrar nuevo lote",
                description = "Ingresa los datos del nuevo grupo de animales al sistema.",
                iconRes = ArxIcons.Camera, // Reemplaza con tus íconos reales
                onClick = {}
            )
        }
    }
}