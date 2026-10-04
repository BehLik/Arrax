package com.example.arrax.core.designsystem.components.backgrounds

import android.provider.Settings
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.components.backgrounds.montain.ArxMountains
import com.example.arrax.core.designsystem.theme.ArxTheme

/**
 * Fondo de pantalla de Arrax.
 * Utiliza MaterialTheme.colorScheme para garantizar compatibilidad Light/Dark.
 */
@Composable
fun ArxBackground(
    modifier: Modifier = Modifier,
    showMountains: Boolean = false,
    animated: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    val context = LocalContext.current
    val animationsOn = remember {
        animated && Settings.Global.getFloat(
            context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f
        ) > 0f
    }

    val transition = rememberInfiniteTransition(label = "arxBg")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(14_000, easing = LinearEasing), RepeatMode.Reverse),
        label = "arxBgPhase",
    )

    // Tokens de color basados en el Theme de Arrax
    val backgroundColor = MaterialTheme.colorScheme.background
    val glowColor = MaterialTheme.colorScheme.primary // Emerald action color para el glow

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(backgroundColor)
                val p = if (animationsOn) phase else 0.5f
                drawRect(
                    Brush.radialGradient(
                        colors = listOf(glowColor.copy(alpha = 0.12f), Color.Transparent),
                        center = Offset(size.width * (0.2f + 0.6f * p), size.height * 0.12f),
                        radius = size.width * 0.95f,
                    )
                )
            }
    ) {
        if (showMountains) {
            ArxMountains(Modifier.align(Alignment.BottomCenter))
        }
        content()
    }
}

@Preview(
    name = "Light Mode - No Mountains",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun ArxBackgroundPreviewLight() {
    ArxTheme {
        ArxBackground(
            showMountains =true,
            animated = false // Apagado en preview para mejor rendimiento
        ) {
            Text(
                text = "Arx Background (Light)",
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

// Preview: Modo Oscuro con montañas
@Preview(
    name = "Dark Mode - Mountains",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ArxBackgroundPreviewDark() {
    ArxTheme(darkTheme = true) {
        ArxBackground(
            showMountains = true,
            animated = false
        ) {
            Text(
                text = "Arx Background + Mountains (Dark)",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 64.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}