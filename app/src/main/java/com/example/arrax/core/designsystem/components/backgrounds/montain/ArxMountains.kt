package com.example.arrax.core.designsystem.components.backgrounds.montain

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.theme.ArxTheme
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

/**
 * Paisaje de montañas en capas con bruma y bosque de pinos, dibujado 100% en Canvas.
 *
 * - Sin imágenes externas: todo se genera proceduralmente (determinista, siempre igual).
 * - Colores derivados de MaterialTheme (background + primary) -> Light/Dark automático.
 * - Se adapta al ancho disponible; el alto lo define [aspectRatio] (ancho / alto).
 * - Se cachea con drawWithCache: solo se reconstruye si cambia el tamaño o el tema.
 * - graphicsLayer lo aísla para que la animación del glow de ArxBackground no lo redibuje.
 */
@Composable
fun ArxMountains(
    modifier: Modifier = Modifier,
    aspectRatio: Float = 1.2f,
) {
    val colors = MaterialTheme.colorScheme
    val palette = remember(colors.background, colors.primary) {
        MountainPalette.from(colors.background, colors.primary)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .graphicsLayer { }
            .drawWithCache {
                val shapes = buildShapes(size, palette)
                onDrawBehind { shapes.forEach { it.draw(this) } }
            }
    )
}

// ───────────────────────────── Paleta ─────────────────────────────

private class MountainPalette(
    val fog: Color,   // tono más lejano / bruma
    val mid: Color,   // tono intermedio
    val deep: Color,  // tono del primer plano
    val mist: Color,  // bandas de niebla entre capas
) {
    /** t = 0 (lejano) .. 1 (primer plano) */
    fun depth(t: Float): Color =
        if (t < 0.55f) lerp(fog, mid, t / 0.55f)
        else lerp(mid, deep, (t - 0.55f) / 0.45f)

    companion object {
        fun from(background: Color, primary: Color): MountainPalette {
            val dark = background.luminance() < 0.5f
            return if (dark) {
                val fog = lerp(background, primary, 0.22f)
                MountainPalette(
                    fog = fog,
                    mid = lerp(background, primary, 0.10f),
                    deep = lerp(background, Color.Black, 0.70f),
                    mist = fog.copy(alpha = 0.28f),
                )
            } else {
                val fog = lerp(background, primary, 0.28f)
                MountainPalette(
                    fog = fog,
                    mid = lerp(primary, Color.Black, 0.35f),
                    deep = lerp(primary, Color.Black, 0.86f),
                    mist = fog.copy(alpha = 0.40f),
                )
            }
        }
    }
}

// ───────────────────────────── Capas ─────────────────────────────

/**
 * base/amp/treeH son fracciones del alto del lienzo.
 * sharp: 0 = lomas suaves, 1 = picos filosos.
 * haze: cuánto se aclara la capa hacia abajo (degradado vertical).
 */
private data class LayerSpec(
    val seed: Int,
    val base: Float,
    val amp: Float,
    val sharp: Float,
    val haze: Float,
    val treeH: Float = 0f,
    val mist: Boolean = false,
)

private val LAYERS = listOf(
    // Lejanas: sin árboles, tonos tenues
    LayerSpec(1, 0.36f, 0.12f, 0.35f, haze = 0.45f),
    LayerSpec(2, 0.43f, 0.15f, 0.40f, haze = 0.45f),
    LayerSpec(3, 0.50f, 0.17f, 0.35f, haze = 0.40f),
    LayerSpec(4, 0.57f, 0.18f, 0.30f, haze = 0.40f),
    // Intermedias: curvas suaves con arbolitos diminutos en la cresta
    LayerSpec(5, 0.64f, 0.20f, 0.25f, haze = 0.35f, treeH = 0.030f),
    LayerSpec(6, 0.72f, 0.20f, 0.20f, haze = 0.30f, treeH = 0.035f, mist = true),
    // Bosque: pinos de distintas alturas
    LayerSpec(7, 0.84f, 0.16f, 0.15f, haze = 0.25f, treeH = 0.10f, mist = true),
    LayerSpec(8, 0.93f, 0.12f, 0.10f, haze = 0.20f, treeH = 0.13f, mist = true),
    LayerSpec(9, 1.03f, 0.08f, 0.10f, haze = 0f, treeH = 0.17f),
)

private class Band(val top: Float, val height: Float, val brush: Brush)

private class LayerShape(
    val fill: Path,
    val trees: Path?,
    val brush: Brush,
    val treeColor: Color,
    val band: Band?,
) {
    fun draw(scope: DrawScope) = with(scope) {
        drawPath(fill, brush)
        trees?.let { drawPath(it, treeColor) }
        band?.let {
            drawRect(
                brush = it.brush,
                topLeft = Offset(0f, it.top),
                size = Size(size.width, it.height),
            )
        }
    }
}

// ───────────────────────────── Geometría ─────────────────────────────

private val TAU = (2.0 * PI).toFloat()
private val PI_F = PI.toFloat()

/** Altura normalizada (0..1) de la cresta en x (0..1). Mezcla de lomas suaves y picos. */
private fun ridge(x: Float, seed: Int, sharp: Float): Float {
    val s = seed * 1.7f
    val smooth = 0.50f * sin(TAU * 1.3f * x + s) +
            0.30f * sin(TAU * 2.9f * x + s * 2.3f) +
            0.20f * sin(TAU * 5.7f * x + s * 3.1f)
    val r1 = 1f - 2f * abs(sin(PI_F * 2.2f * x + s * 1.3f))
    val r2 = 1f - 2f * abs(sin(PI_F * 3.7f * x + s * 0.7f))
    val ridged = 0.6f * r1 + 0.4f * r2
    val v = smooth * (1f - sharp) + ridged * sharp
    return ((v * 1.25f + 1f) / 2f).coerceIn(0f, 1f)
}

/** Pino de varios pisos (triángulos del mismo sentido -> se unen con fill NonZero). */
private fun Path.pine(cx: Float, baseY: Float, h: Float) {
    val top = baseY - h
    val half = h * 0.17f
    val tiers = 5
    for (k in 0 until tiers) {
        val tt = top + h * 0.16f * k
        val tb = tt + h * 0.30f
        val hw = half * (0.45f + 0.55f * (k + 1) / tiers)
        moveTo(cx, tt)
        lineTo(cx + hw, tb)
        lineTo(cx - hw, tb)
        close()
    }
    // tronco (mismo sentido de giro que los triángulos)
    val t = h * 0.015f
    val y0 = top + h * 0.80f
    moveTo(cx - t, y0)
    lineTo(cx + t, y0)
    lineTo(cx + t, baseY)
    lineTo(cx - t, baseY)
    close()
}

private fun buildShapes(size: Size, palette: MountainPalette): List<LayerShape> {
    val w = size.width
    val h = size.height
    val steps = 160

    return LAYERS.mapIndexed { index, spec ->
        val t = index / (LAYERS.size - 1f)
        val color = palette.depth(t)

        val ys = FloatArray(steps + 1) { s ->
            (spec.base - spec.amp * ridge(s / steps.toFloat(), spec.seed, spec.sharp)) * h
        }

        val fill = Path().apply {
            moveTo(0f, h)
            for (s in 0..steps) lineTo(w * s / steps, ys[s])
            lineTo(w, h)
            close()
        }

        val trees = if (spec.treeH > 0f) {
            Path().apply {
                val rnd = Random(spec.seed * 7919)
                val treeH = spec.treeH * h
                var x = -treeH * 0.2f
                while (x < w + treeH * 0.2f) {
                    val xi = (x / w).coerceIn(0f, 1f)
                    val ridgeY = ys[(xi * steps).toInt()]
                    val cluster = 0.8f + 0.2f * sin(TAU * 6f * xi + spec.seed)
                    val th = treeH * (0.55f + 0.6f * rnd.nextFloat()) * cluster
                    pine(x, ridgeY + treeH * 0.25f, th)
                    x += treeH * (0.16f + 0.20f * rnd.nextFloat())
                }
            }
        } else null

        // Con árboles: la parte visible arriba del valle más bajo es color sólido
        // (para que cuadre con las siluetas); sin árboles: degradado desde la cresta.
        val startY = if (spec.treeH > 0f) ys.max() else ys.min()
        val brush = Brush.verticalGradient(
            colors = listOf(color, lerp(color, palette.fog, spec.haze)),
            startY = startY,
            endY = h,
        )

        val band = if (spec.mist) {
            val bandH = h * 0.16f
            val top = spec.base * h - bandH * 0.5f
            Band(
                top = top,
                height = bandH,
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, palette.mist, Color.Transparent),
                    startY = top,
                    endY = top + bandH,
                ),
            )
        } else null

        LayerShape(fill, trees, brush, color, band)
    }
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