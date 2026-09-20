package com.example.arrax.core.designsystem.components.voice

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.theme.ArxTheme

@Composable
fun ArxVoiceWaveform(
    isListening: Boolean,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary
) {
    // Definimos duraciones aleatorias para que las barras se muevan de forma asíncrona
    val animationDurations = listOf(500, 700, 400, 600, 800)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        animationDurations.forEach { duration ->
            ArxWaveBar(
                isListening = isListening,
                durationMillis = duration,
                barColor = barColor
            )
        }
    }
}

@Composable
private fun ArxWaveBar(
    isListening: Boolean,
    durationMillis: Int,
    barColor: Color
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave_transition")

    val heightMultiplier by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = if (isListening) 1f else 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_height"
    )

    // Altura base 8dp, altura máxima 32dp
    val barHeight = 8.dp + (24.dp * if (isListening) heightMultiplier else 0f)

    Box(
        modifier = Modifier
            .width(6.dp)
            .height(barHeight)
            .clip(CircleShape)
            .background(if (isListening) barColor else MaterialTheme.colorScheme.surfaceVariant)
    )
}

@Preview(showBackground = true)
@Composable
fun ArxVoiceWaveformPreview() {
    ArxTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
            ArxVoiceWaveform(isListening = false) // Estado inactivo
            ArxVoiceWaveform(isListening = true)  // Estado escuchando
        }
    }
}