package com.example.arrax.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Definimos la personalidad de los bordes
val ArxShapes = Shapes(
    // Extra Pequeño (4dp): Para etiquetas muy chiquitas, tooltips o badges
    extraSmall = RoundedCornerShape(4.dp),

    // Pequeño (8dp): Para Botones, Chips de selección
    small = RoundedCornerShape(8.dp),

    // Medio (16dp): El estándar para Tarjetas (Cards)
    medium = RoundedCornerShape(16.dp),

    // Grande (24dp): Ideal para los BottomSheets
    large = RoundedCornerShape(24.dp),

    // Extra Grande (32dp): Para componentes muy amplios, como una hoja que cubre casi toda la pantalla.
    extraLarge = RoundedCornerShape(32.dp)
)