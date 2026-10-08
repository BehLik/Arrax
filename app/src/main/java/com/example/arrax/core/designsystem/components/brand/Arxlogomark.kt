package com.example.arrax.core.designsystem.components.brand

import android.content.res.Configuration
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

    /** Fracción del lado del contenedor que se deja como aire alrededor del isotipo. */
    const val ContentPadding = 0.20f

    /** Radio de esquina como fracción del lado (como el icono del mockup). */
    const val CornerFraction = 0.26f

    @Composable
    internal fun isDark(): Boolean = MaterialTheme.colorScheme.background.luminance() < 0.5f

    @Composable
    fun borderColor(): Color =
        if (isDark()) ArxBrand.Emerald.copy(alpha = 0.90f) else ArxBrand.ForestVariant

    /** Intensidad del halo: visible en Dark, apagado en Light (igual que el mockup). */
    @Composable
    fun glow(): Float = if (isDark()) 0.35f else 0f
}

/**
 * Isotipo de Arrax (A blanca + chevron verde). El dibujo es blanco, por eso SOLO se ve sobre
 * fondo oscuro: úsalo con [withContainer] = true (login, splash) o sobre un banner oscuro.
 *
 * @param size           lado del contenedor (o alto del isotipo si [withContainer] = false).
 * @param withContainer  true = cuadrado redondeado oscuro con borde y glow.
 * @param containerColor color del contenedor (oscuro en Light y en Dark, como el mockup).
 * @param cornerRadius   radio de esquina del contenedor.
 * @param borderColor    color del borde (se difumina en degradado).
 * @param glow           0f..1f, intensidad del halo exterior. 0f = sin glow.
 * @param glowColor      color del halo.
 * @param contentPadding fracción del lado reservada como aire dentro del contenedor.
 * @param contentDescription null = decorativo (TalkBack lo ignora).
 */
@Composable
fun ArxLogoMark(
    modifier: Modifier = Modifier,
    size: Dp = 96.dp,
    withContainer: Boolean = true,
    containerColor: Color = ArxBrand.Forest,
    shape: androidx.compose.ui.graphics.Shape = ArxShapes.large,
//    cornerRadius: Dp = size * ArxLogoMarkDefaults.CornerFraction,
    borderColor: Color = ArxLogoMarkDefaults.borderColor(),
    glow: Float = ArxLogoMarkDefaults.glow(),
    glowColor: Color = ArxBrand.Emerald,
    contentPadding: Float = ArxLogoMarkDefaults.ContentPadding,
    contentDescription: String? = null,
) {
    val painter = painterResource(R.drawable.logo_mark)

    if (!withContainer) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            modifier = modifier
                .height(size)
                .aspectRatio(ArxLogoMarkDefaults.Aspect),
        )
    } else {
//        val shape = RoundedCornerShape(cornerRadius)
        val borderBrush = Brush.linearGradient(
            listOf(borderColor, borderColor.copy(alpha = borderColor.alpha * 0.35f), borderColor)
        )
        Box(
            modifier = modifier
                .size(size)
                .arxGlow(glow, glowColor)
                .clip(shape)
                .background(containerColor)
                .border(1.dp, borderBrush, shape),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(size * contentPadding),
            )
        }
    }
}

/** Halo radial detrás del contenedor (funciona en cualquier API, a diferencia de shadow con color). */
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
        Row(Modifier
            .padding(ArxSpacing.xl),
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)) {
            ArxLogoMark(size = 40.dp, shape = ArxShapes.small)
            ArxLogoMark(size = 72.dp, shape = ArxShapes.medium)
            ArxLogoMark(size = 112.dp) // Usa el default (large)
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
                .padding(32.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ArxLogoMark(size = 40.dp, shape = ArxShapes.small)
            ArxLogoMark(size = 72.dp, shape = ArxShapes.medium)
            ArxLogoMark(size = 112.dp) // Usa el default (large)
        }
    }
}