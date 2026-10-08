package com.example.arrax.core.designsystem.components.backgrounds

import android.content.res.Configuration
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.backgrounds.montain.ArxMountains
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

/**
 * Fondo de pantalla de Arrax.
 *
 * @param showMountains true = muestra la ilustración de montañas (res/drawable/bg_mountains.xml)
 *                      abajo, recoloreada con el tema; false = la oculta.
 * @param invertMountains pon true si en tu SVG lo cercano es blanco y lo lejano negro.
 * @param animated      true = el glow superior se mueve (se apaga solo si el sistema
 *                      tiene las animaciones desactivadas).
 *
 * Usa MaterialTheme.colorScheme para ser compatible con Light/Dark.
 */
@Composable
fun ArxBackground(
    modifier: Modifier = Modifier,
    showMountains: Boolean = false,
    invertMountains: Boolean = false,
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

    val backgroundColor = MaterialTheme.colorScheme.background
    val glowColor = MaterialTheme.colorScheme.primary

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
        // El booleano decide si las montañas aparecen (con fundido suave).
        AnimatedVisibility(
            visible = showMountains,
            enter = fadeIn(tween(400)),
            exit = fadeOut(tween(300)),
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            ArxMountains(invert = invertMountains)
        }
        content()
    }
}

// ───────────────────────────── Previews ─────────────────────────────

@Preview(name = "Light - Mountains ON", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ArxBackgroundPreviewLightOn() {
    ArxTheme {
        ArxBackground(showMountains = true, animated = false) {
            Text(
                text = "Arx Background (Light)",
                modifier = Modifier.align(Alignment.TopCenter).padding(ArxSpacing.giant),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Preview(name = "Light - Mountains OFF", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ArxBackgroundPreviewLightOff() {
    ArxTheme {
        ArxBackground(showMountains = false, animated = false) {
            Text(
                text = "Sin montañas",
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Preview(name = "Dark - Mountains ON", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ArxBackgroundPreviewDark() {
    ArxTheme(darkTheme = true) {
        ArxBackground(showMountains = true, animated = false) {
            Text(
                text = "Arx Background (Dark)",
                modifier = Modifier.align(Alignment.TopCenter).padding(ArxSpacing.giant),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}