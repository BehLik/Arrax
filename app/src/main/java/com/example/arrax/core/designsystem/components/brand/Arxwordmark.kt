package com.example.arrax.core.designsystem.components.brand

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.arrax.R
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

object ArxWordmarkDefaults {
    /** Aspecto del drawable recortado: 3056 x 813. */
    const val Aspect = 3056f / 813f

    /** Colores originales del XML: texto y triángulo de la "A". */
    val AssetText = Color(0xFF16292B)
//    val AssetAccent = Color(0xFFFFFFFF)
    val AssetAccent = Color(0xFF0BAA91)
}

/**
 * Nombre "Arrax". El XML trae dos colores (texto #16292B y triángulo #0BAA91); con un
 * duotono se remapean a [textColor] y [accentColor], así funciona en Light y en Dark
 * (el texto original casi no se ve sobre fondo oscuro).
 *
 * @param height      alto del wordmark; el ancho sale del aspecto (~3.76:1).
 * @param textColor   color de las letras. Por defecto onBackground del tema.
 * @param accentColor color del triángulo de la A. Por defecto primary del tema;
 *                    pasa [ArxWordmarkDefaults.AssetAccent] para conservar el teal original.
 */
@Composable
fun ArxWordmark(
    modifier: Modifier = Modifier,
    height: Dp = 32.dp,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    contentDescription: String? = null,
) {
    val filter = remember(textColor, accentColor) {
        twoTone(
            srcLow = ArxWordmarkDefaults.AssetText,
            srcHigh = ArxWordmarkDefaults.AssetAccent,
            dstLow = textColor,
            dstHigh = accentColor,
        )
    }
    Image(
        painter = painterResource(R.drawable.logo_wordmark),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        colorFilter = filter,
        modifier = modifier
            .height(height)
            .aspectRatio(ArxWordmarkDefaults.Aspect),
    )
}

private fun Color.rawLuma(): Float = 0.2126f * red + 0.7152f * green + 0.0722f * blue

/**
 * Generalización del duotone de ArxMountains: en lugar de mapear negro→lo y blanco→hi,
 * mapea la luminosidad de [srcLow] a [dstLow] y la de [srcHigh] a [dstHigh]
 * (interpolación lineal por canal; el alfa no se toca).
 */
private fun twoTone(srcLow: Color, srcHigh: Color, dstLow: Color, dstHigh: Color): ColorFilter {
    val l0 = srcLow.rawLuma()
    val span = srcHigh.rawLuma() - l0

    fun row(lo: Float, hi: Float): FloatArray {
        val k = (hi - lo) / span
        return floatArrayOf(k * 0.2126f, k * 0.7152f, k * 0.0722f, 0f, (lo - k * l0) * 255f)
    }

    val matrix = ColorMatrix(
        floatArrayOf(
            *row(dstLow.red, dstHigh.red),
            *row(dstLow.green, dstHigh.green),
            *row(dstLow.blue, dstHigh.blue),
            0f, 0f, 0f, 1f, 0f,
        )
    )
    return ColorFilter.colorMatrix(matrix)
}

// ───────────────────────────── Previews ─────────────────────────────

@Preview(name = "Wordmark - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ArxWordmarkPreviewLight() {
    ArxTheme {
        Column(Modifier.padding(ArxSpacing.xl)) { ArxWordmark(height = 40.dp) }
    }
}

@Preview(name = "Wordmark - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ArxWordmarkPreviewDark() {
    ArxTheme(darkTheme = true) {
        Column(Modifier.background(MaterialTheme.colorScheme.background).padding(ArxSpacing.xl)) {
            ArxWordmark(height = 40.dp)
        }
    }
}