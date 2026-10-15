package com.example.arrax.core.designsystem.components.brand

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.arrax.R
import com.example.arrax.core.designsystem.theme.ArxBrand
import com.example.arrax.core.designsystem.theme.ArxShapes
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

object ArxLogoMarkDefaults {
    /** Aspecto del drawable recortado: 1235 x 1114. */
    const val Aspect = 1235f / 1114f
    const val ContentPadding = 0.20f

    @Composable
    internal fun isDark(): Boolean = MaterialTheme.colorScheme.background.luminance() < 0.5f

    @Composable
    fun borderColor(): Color =
        if (isDark()) ArxBrand.Primary.copy(alpha = 0.90f) else ArxBrand.ForestVariant

    @Composable
    fun glow(): Float = if (isDark()) 0.35f else 0f

    /**
     * Color del chevron exterior cuando NO hay contenedor:
     * oscuro en Light (contraste sobre fondo claro), Unspecified = original (blanco) en Dark.
     */
    @Composable
    fun outerColor(): Color = if (isDark()) Color.Unspecified else ArxBrand.Forest
}

@Composable
fun ArxLogoMark(
    modifier: Modifier = Modifier,
    size: Dp = 96.dp,
    withContainer: Boolean = true,
    containerColor: Color = ArxBrand.Forest,
    shape: androidx.compose.ui.graphics.Shape = ArxShapes.large,
    borderColor: Color = ArxLogoMarkDefaults.borderColor(),
    glow: Float = ArxLogoMarkDefaults.glow(),
    glowColor: Color = ArxBrand.Primary,
    contentPadding: Float = ArxLogoMarkDefaults.ContentPadding,
    animatedSweep: Boolean = true,
    outerColor: Color = ArxLogoMarkDefaults.outerColor(), // solo aplica con withContainer = false
    contentDescription: String? = null,
) {
    if (!withContainer) {
        // Dos capas para teñir solo el chevron exterior sin tocar el interior verde.
        Box(
            modifier = modifier
                .height(size)
                .aspectRatio(ArxLogoMarkDefaults.Aspect),
        ) {
            Image(
                painter = painterResource(R.drawable.logo_mark_inner),
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
            )
            Image(
                painter = painterResource(R.drawable.logo_mark_outer),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                colorFilter = if (outerColor.isSpecified) ColorFilter.tint(outerColor) else null,
                modifier = Modifier.fillMaxSize(),
            )
        }
    } else {
        val borderBrush = Brush.linearGradient(
            listOf(borderColor, borderColor.copy(alpha = borderColor.alpha * 0.35f), borderColor)
        )
        Box(
            modifier = modifier
                .size(size)
                .arxGlow(glow, glowColor)
                .clip(shape)
                .background(containerColor)
                .arxSweepLight(glowColor, enabled = animatedSweep && glow > 0f)
                .border(1.dp, borderBrush, shape),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.logo_mark),
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(size * contentPadding),
            )
        }
    }
}

/** Barrido de luz diagonal (Neominimalismo / Escáner activo). */
private fun Modifier.arxSweepLight(color: Color, enabled: Boolean): Modifier =
    if (!enabled) this
    else composed {
        val transition = rememberInfiniteTransition(label = "arxSweep")
        val progress by transition.animateFloat(
            initialValue = -0.5f,
            targetValue = 1.5f, // más allá de 1.0 para que el barrido salga completo del contenedor
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "sweepProgress",
        )

        drawWithCache {
            val w = size.width
            val h = size.height
            val posX = w * progress
            val posY = h * progress

            val sweepBrush = Brush.linearGradient(
                colors = listOf(
                    Color.Transparent,
                    color.copy(alpha = 0.40f),
                    Color.Transparent,
                ),
                start = Offset(posX - w * 0.4f, posY - h * 0.4f),
                end = Offset(posX + w * 0.4f, posY + h * 0.4f),
            )

            onDrawBehind {
                drawRect(brush = sweepBrush)
            }
        }
    }

/** Halo radial detrás del contenedor. */
private fun Modifier.arxGlow(intensity: Float, color: Color): Modifier =
    if (intensity <= 0f) this
    else drawBehind {
        val r = size.minDimension * 0.95f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color.copy(alpha = intensity), Color.Transparent),
                center = center,
                radius = r,
            ),
            radius = r,
            center = center,
        )
    }

// ───────────────────────────── Previews ─────────────────────────────

@Preview(name = "Mark - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ArxLogoMarkPreviewLight() {
    ArxTheme {
        Row(
            Modifier.padding(ArxSpacing.xl),
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ArxLogoMark(size = 40.dp, shape = ArxShapes.small)
            ArxLogoMark(size = 72.dp, shape = ArxShapes.medium)
            ArxLogoMark(size = 112.dp)
            ArxLogoMark(size = 72.dp, withContainer = false) // chevron exterior oscuro
        }
    }
}

@Preview(name = "Mark - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ArxLogoMarkPreviewDark() {
    ArxTheme(darkTheme = true) {
        Row(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(ArxSpacing.xl),
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.lg),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ArxLogoMark(size = 40.dp, shape = ArxShapes.small)
            ArxLogoMark(size = 72.dp, shape = ArxShapes.medium)
            ArxLogoMark(size = 112.dp)
            ArxLogoMark(size = 72.dp, withContainer = false) // chevron exterior blanco
        }
    }
}