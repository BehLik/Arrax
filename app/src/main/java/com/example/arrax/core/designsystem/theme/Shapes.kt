package com.example.arrax.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val ArxShapes = Shapes(
    // Badges, tooltips
    extraSmall = RoundedCornerShape(6.dp),
    // Campos de texto (10dp en el JSON) y chips: usa small
    small = RoundedCornerShape(10.dp),
    // Botones (12dp) y tarjetas (14-16dp)
    medium = RoundedCornerShape(14.dp),
    // Diálogos (16dp) y BottomSheets
    large = RoundedCornerShape(20.dp),
    // Hojas casi a pantalla completa
    extraLarge = RoundedCornerShape(32.dp)
)