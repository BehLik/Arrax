package com.example.arrax.core.designsystem.components.voice

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

/** Estados del asistente de voz (voiceStates del JSON). */
enum class ArxVoiceState(val label: String) {
    Idle("Inactivo"),
    Listening("Escuchando"),
    Processing("Procesando"),
    Recognized("Pedido detectado"),
    AwaitingConfirmation("Esperando confirmación"),
    Saving("Guardando"),
    Completed("Confirmado"),
    Error("Error / Reintentar")
}

/** Color semántico del estado, siempre desde el tema. */
@Composable
fun arxVoiceStateColor(state: ArxVoiceState): Color = when (state) {
    ArxVoiceState.Idle -> MaterialTheme.colorScheme.onSurfaceVariant
    ArxVoiceState.Listening -> MaterialTheme.colorScheme.primary
    ArxVoiceState.Processing -> MaterialTheme.colorScheme.secondary
    ArxVoiceState.Recognized -> MaterialTheme.extendedColors.success.color
    ArxVoiceState.AwaitingConfirmation -> MaterialTheme.extendedColors.warning.color
    ArxVoiceState.Saving -> MaterialTheme.colorScheme.secondary
    ArxVoiceState.Completed -> MaterialTheme.extendedColors.success.color
    ArxVoiceState.Error -> MaterialTheme.colorScheme.error
}

/**
 * Etiqueta de estado: punto de color + texto. El texto siempre está presente
 * porque el color no puede ser el único indicador del estado (accesibilidad).
 */
@Composable
fun ArxVoiceStateLabel(
    state: ArxVoiceState,
    modifier: Modifier = Modifier,
    label: String = state.label
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(arxVoiceStateColor(state))
        )
        Spacer(modifier = Modifier.width(ArxSpacing.xs))
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ArxVoiceStatePreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(ArxSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
            ) {
                ArxVoiceState.entries.forEach { ArxVoiceStateLabel(state = it) }
            }
        }
    }
}

@Preview(name = "Estados de voz – Light", showBackground = false)
@Composable
fun ArxVoiceStatePreviewLight() = ArxVoiceStatePreviewContent()

@Preview(name = "Estados de voz – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxVoiceStatePreviewDark() = ArxVoiceStatePreviewContent()