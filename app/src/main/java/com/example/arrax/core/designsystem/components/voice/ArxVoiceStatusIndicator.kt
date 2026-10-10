package com.example.arrax.core.designsystem.components.voice

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

/** Insignia compacta "Manos libres" con punto que palpita. Solo se muestra mientras escucha. */
@Composable
fun ArxVoiceStatusIndicator(
    isListening: Boolean,
    modifier: Modifier = Modifier
) {
    if (!isListening) return

    val transition = rememberInfiniteTransition(label = "pulse")
    val dotAlpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )
    val shape = MaterialTheme.shapes.small

    Row(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.extendedColors.glass)
            .border(1.dp, MaterialTheme.extendedColors.glassBorder, shape)
            .padding(horizontal = ArxSpacing.xs, vertical = ArxSpacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .alpha(dotAlpha)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.error)
        )
        Spacer(modifier = Modifier.width(ArxSpacing.xs))
        Icon(
            painter = painterResource(id = ArxIcons.Mic),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(ArxSpacing.xxs))
        Text(
            text = "Manos libres",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ArxVoiceStatusIndicatorPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ArxVoiceStatusIndicator(
                isListening = true,
                modifier = Modifier.padding(ArxSpacing.md)
            )
        }
    }
}

@Preview(name = "Indicador de voz – Light", showBackground = false)
@Composable
fun ArxVoiceStatusIndicatorPreviewLight() = ArxVoiceStatusIndicatorPreviewContent()

@Preview(name = "Indicador de voz – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxVoiceStatusIndicatorPreviewDark() = ArxVoiceStatusIndicatorPreviewContent()