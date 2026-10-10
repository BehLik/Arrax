package com.example.arrax.core.designsystem.components.voice

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import kotlin.math.PI
import kotlin.math.sin

/**
 * Visualizador de ondas de audio (barras verticales).
 * - Con [amplitudes] (0..1 por barra): reacciona al volumen real del micrófono.
 * - Sin [amplitudes]: animación suave en bucle (fallback del JSON).
 * - Con [isListening] = false: barras quietas.
 */
@Composable
fun ArxVoiceWaveform(
    isListening: Boolean,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary,
    amplitudes: List<Float>? = null,
    barCount: Int = 5
) {
    val count = (amplitudes?.size ?: barCount).coerceAtLeast(1)
    val idleColor = MaterialTheme.colorScheme.outline
    val phase = rememberWavePhase(active = isListening && amplitudes == null)

    val levels = List(count) { i ->
        when {
            !isListening -> 0f
            amplitudes != null -> {
                val target = amplitudes[i].coerceIn(0f, 1f)
                animateFloatAsState(
                    targetValue = target,
                    animationSpec = tween(100),
                    label = "wave_bar"
                ).value
            }
            else -> 0.5f + 0.5f * sin(2f * PI.toFloat() * (phase + i / count.toFloat()))
        }
    }

    val barWidth = 6.dp
    val gap = 4.dp
    val minHeight = 8.dp
    val maxHeight = 32.dp

    Canvas(
        modifier = modifier
            .size(width = barWidth * count + gap * (count - 1), height = maxHeight)
            .clearAndSetSemantics { } // decorativo: el estado lo anuncia el texto
    ) {
        val bw = barWidth.toPx()
        val g = gap.toPx()
        val mn = minHeight.toPx()
        val color = if (isListening) barColor else idleColor
        levels.forEachIndexed { i, level ->
            val h = mn + (size.height - mn) * level
            drawRoundRect(
                color = color,
                topLeft = Offset(x = i * (bw + g), y = (size.height - h) / 2f),
                size = Size(bw, h),
                cornerRadius = CornerRadius(bw / 2f)
            )
        }
    }
}

// Solo crea la transición infinita cuando hace falta (no hay animación permanente en reposo).
@Composable
private fun rememberWavePhase(active: Boolean): Float {
    if (!active) return 0f
    val transition = rememberInfiniteTransition(label = "wave_phase")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )
    return phase
}

@Composable
private fun ArxVoiceWaveformPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(ArxSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.xl)
            ) {
                ArxVoiceWaveform(isListening = false)
                ArxVoiceWaveform(isListening = true)
                ArxVoiceWaveform(
                    isListening = true,
                    amplitudes = listOf(0.2f, 0.6f, 1f, 0.7f, 0.3f, 0.5f, 0.9f, 0.4f, 0.2f)
                )
            }
        }
    }
}

@Preview(name = "Ondas – Light", showBackground = false)
@Composable
fun ArxVoiceWaveformPreviewLight() = ArxVoiceWaveformPreviewContent()

@Preview(name = "Ondas – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxVoiceWaveformPreviewDark() = ArxVoiceWaveformPreviewContent()