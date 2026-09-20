package com.example.arrax.core.designsystem.components.cards

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
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.icons.ArxIcons

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


@Preview(showBackground = true, name = "Familia de Cards Arrax")
@Composable
fun ArxCardsPreview() {
    ArxTheme {
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
                    modifier = Modifier.weight(1f)
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