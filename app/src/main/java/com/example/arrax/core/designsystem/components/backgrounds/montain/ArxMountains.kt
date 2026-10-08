package com.example.arrax.core.designsystem.components.backgrounds.montain

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.R
import com.example.arrax.core.designsystem.theme.ArxTheme

/**
 * Montañas a partir de UN solo Vector Drawable en blanco y negro: res/drawable/bg_mountains.xml
 *
 * Con [themed] = true se aplica un "duotono": el negro del dibujo se pinta con un verde oscuro
 * del tema y el blanco con un verde claro (cerca del background). Los grises intermedios
 * quedan entre ambos, así se conserva la profundidad de las capas, en Light y en Dark.
 *
 * @param fadeTop difumina el borde superior (efecto niebla).
 * @param themed  false = muestra el dibujo tal cual, sin recolorear.
 * @param invert  true si en tu SVG lo cercano es blanco y lo lejano negro (al revés de lo normal).
 */
@Composable
fun ArxMountains(
    modifier: Modifier = Modifier,
    fadeTop: Boolean = true,
    themed: Boolean = true,
    invert: Boolean = false,
) {
    val colors = MaterialTheme.colorScheme
    val filter = remember(colors.background, colors.primary, themed, invert) {
        if (themed) duotone(colors.background, colors.primary, invert) else null
    }

    Image(
        painter = painterResource(R.drawable.bg_mountains),
        contentDescription = null, // decorativa: TalkBack la ignora
        contentScale = ContentScale.FillWidth,
        colorFilter = filter,
        modifier = modifier
            .fillMaxWidth()
            .then(if (fadeTop) Modifier.topFade() else Modifier)
    )
}

/** Convierte la luminosidad (negro..blanco) en un degradado entre dos colores del tema. */
private fun duotone(background: Color, primary: Color, invert: Boolean): ColorFilter {
    val dark = background.luminance() < 0.5f
    val far = if (dark) lerp(background, primary, 0.14f) else lerp(background, primary, 0.20f)
    val near = if (dark) lerp(background, Color.Black, 0.75f) else lerp(primary, Color.Black, 0.60f)

    val lo = if (invert) far else near // color para el negro del SVG
    val hi = if (invert) near else far // color para el blanco del SVG

    fun row(l: Float, h: Float): FloatArray {
        val d = h - l
        return floatArrayOf(d * 0.2126f, d * 0.7152f, d * 0.0722f, 0f, l * 255f)
    }

    val matrix = ColorMatrix(
        floatArrayOf(
            *row(lo.red, hi.red),
            *row(lo.green, hi.green),
            *row(lo.blue, hi.blue),
            0f, 0f, 0f, 1f, 0f, // el alfa no se toca
        )
    )
    return ColorFilter.colorMatrix(matrix)
}

/** Máscara: el 35 % superior pasa de transparente a opaco. */
private fun Modifier.topFade(): Modifier = this
    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .drawWithContent {
        drawContent()
        drawRect(
            brush = Brush.verticalGradient(
                0f to Color.Transparent,
                0.99f to Color.Black,
                startY = 0f,
                endY = size.height,
            ),
            blendMode = BlendMode.DstIn,
        )
    }

// ───────────────────────────── Previews ─────────────────────────────

@Preview(name = "Mountains - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ArxMountainsPreviewLight() {
    ArxTheme {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            ArxMountains(Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Preview(name = "Mountains - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ArxMountainsPreviewDark() {
    ArxTheme(darkTheme = true) {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            ArxMountains(Modifier.align(Alignment.BottomCenter))
        }
    }
}