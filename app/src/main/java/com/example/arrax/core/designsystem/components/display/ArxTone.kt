package com.example.arrax.core.designsystem.components.display

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.arrax.core.designsystem.theme.extendedColors

/** Tono semántico. Se resuelve siempre con tokens del tema. */
enum class ArxTone { Primary, Success, Warning, Info, Error, Neutral }

@Composable
fun ArxTone.color(): Color = when (this) {
    ArxTone.Primary -> MaterialTheme.colorScheme.primary
    ArxTone.Success -> MaterialTheme.extendedColors.success.color
    ArxTone.Warning -> MaterialTheme.extendedColors.warning.color
    ArxTone.Info -> MaterialTheme.extendedColors.info.color
    ArxTone.Error -> MaterialTheme.colorScheme.error
    ArxTone.Neutral -> MaterialTheme.colorScheme.onSurfaceVariant
}