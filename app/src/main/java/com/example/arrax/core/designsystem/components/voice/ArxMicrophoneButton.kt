package com.example.arrax.core.designsystem.components.voice

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

/**
 * Botón circular de micrófono (reemplaza a ArxFloatingVoiceButton y ArxMicRipple).
 * Estados: Idle, Listening (pulso/halo), Processing (anillo giratorio), Error y deshabilitado.
 * Tap para iniciar / detener. Mínimo 48dp de touch target.
 */
@Composable
fun ArxMicrophoneButton(
    state: ArxVoiceState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = 96.dp
) {
    val isListening = state == ArxVoiceState.Listening
    val isProcessing = state == ArxVoiceState.Processing
    val isError = state == ArxVoiceState.Error

    val containerColor by animateColorAsState(
        targetValue = when {
            !enabled -> MaterialTheme.colorScheme.surfaceVariant
            isListening -> MaterialTheme.colorScheme.primary
            isError -> MaterialTheme.colorScheme.errorContainer
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        label = "mic_container"
    )
    val contentColor by animateColorAsState(
        targetValue = when {
            !enabled -> MaterialTheme.extendedColors.disabled
            isListening -> MaterialTheme.colorScheme.onPrimary
            isError -> MaterialTheme.colorScheme.onErrorContainer
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "mic_content"
    )

    Box(
        modifier = modifier.size(size * 1.5f),
        contentAlignment = Alignment.Center
    ) {
        if (isListening && enabled) {
            MicPulseHalo(size = size, color = MaterialTheme.colorScheme.primary)
        }

        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(containerColor)
                .semantics { stateDescription = state.label }
                .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = ArxIcons.Mic),
                contentDescription = if (isListening) "Detener escucha" else "Activar micrófono",
                tint = contentColor,
                modifier = Modifier.size(size * 0.42f)
            )
        }

        if (isProcessing) {
            CircularProgressIndicator(
                modifier = Modifier.size(size),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = Color.Transparent,
                strokeWidth = 3.dp
            )
        }
    }
}

// Halo decorativo: alpha y escala son literales porque es decoración (1200 ms, como el JSON).
@Composable
private fun MicPulseHalo(size: Dp, color: Color) {
    val transition = rememberInfiniteTransition(label = "mic_pulse")
    val haloScale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "halo_scale"
    )
    val haloAlpha by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "halo_alpha"
    )
    Box(
        modifier = Modifier
            .size(size)
            .graphicsLayer {
                scaleX = haloScale
                scaleY = haloScale
                alpha = haloAlpha
            }
            .clip(CircleShape)
            .background(color)
    )
}

// ───────────── Compatibilidad (obsoletos) ─────────────

@Deprecated(
    message = "Usa ArxMicrophoneButton",
    replaceWith = ReplaceWith(
        "ArxMicrophoneButton(state = if (isListening) ArxVoiceState.Listening else ArxVoiceState.Idle, onClick = onClick, modifier = modifier, size = 64.dp)"
    )
)
@Composable
fun ArxFloatingVoiceButton(
    isListening: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArxMicrophoneButton(
        state = if (isListening) ArxVoiceState.Listening else ArxVoiceState.Idle,
        onClick = onClick,
        modifier = modifier,
        size = 64.dp
    )
}

@Deprecated(
    message = "Usa ArxMicrophoneButton",
    replaceWith = ReplaceWith(
        "ArxMicrophoneButton(state = if (isListening) ArxVoiceState.Listening else ArxVoiceState.Idle, onClick = onClick, modifier = modifier)"
    )
)
@Composable
fun ArxMicRipple(
    isListening: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArxMicrophoneButton(
        state = if (isListening) ArxVoiceState.Listening else ArxVoiceState.Idle,
        onClick = onClick,
        modifier = modifier
    )
}

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

@Composable
private fun ArxMicrophoneButtonPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Row(
                modifier = Modifier.padding(ArxSpacing.md),
                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
            ) {
                ArxMicrophoneButton(state = ArxVoiceState.Idle, onClick = {}, size = 64.dp)
                ArxMicrophoneButton(state = ArxVoiceState.Listening, onClick = {}, size = 64.dp)
                ArxMicrophoneButton(state = ArxVoiceState.Processing, onClick = {}, size = 64.dp)
                ArxMicrophoneButton(state = ArxVoiceState.Error, onClick = {}, size = 64.dp)
                ArxMicrophoneButton(state = ArxVoiceState.Idle, onClick = {}, enabled = false, size = 64.dp)
            }
        }
    }
}

@Preview(name = "Micrófono – Light", showBackground = false)
@Composable
fun ArxMicrophoneButtonPreviewLight() = ArxMicrophoneButtonPreviewContent()

@Preview(name = "Micrófono – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxMicrophoneButtonPreviewDark() = ArxMicrophoneButtonPreviewContent()