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
import kotlin.math.abs
import kotlin.math.floor
import kotlin.random.Random

/**
 * Paisaje de montañas en capas con bruma y bosque de pinos, dibujado 100% en Canvas.
 *
 * - Abajo oscuro; hacia arriba cada capa se aclara hasta casi igualar el background,
 *   así las crestas lejanas se funden con el fondo (sin cortes ni líneas de color).
 * - Relieve con ruido fractal (no se ve repetitivo ni "de seno").
 * - Colores derivados de MaterialTheme (background + primary) -> Light/Dark.
 * - Se adapta al ancho; el alto lo define [aspectRatio] (ancho / alto).
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
            .graphicsLayer { } // aísla: el glow animado de ArxBackground no lo redibuja
            .drawWithCache {
                val scene = buildScene(size, palette)
                onDrawBehind { scene.draw(this) }
            }
    )
}

// ───────────────────────────── Paleta ─────────────────────────────

private class MountainPalette(
    val far: Color,    // casi igual al background (capa más lejana)
    val mid: Color,    // verde medio
    val deep: Color,   // verde casi negro (primer plano)
    val mist: Color,   // bandas de niebla
    val scrimAlpha: Float,
) {
    /** t = 0 (lejano) .. 1 (primer plano). La curva t² mantiene las lejanas muy tenues. */
    fun depth(t: Float): Color {
        val e = t * t
        return if (e < 0.55f) lerp(far, mid, e / 0.55f)
        else lerp(mid, deep, (e - 0.55f) / 0.45f)
    }

    companion object {
        fun from(background: Color, primary: Color): MountainPalette {
            val dark = background.luminance() < 0.5f
            return if (dark) {
                MountainPalette(
                    far = lerp(background, primary, 0.14f),
                    mid = lerp(background, primary, 0.07f),
                    deep = lerp(background, Color.Black, 0.75f),
                    mist = lerp(background, primary, 0.18f).copy(alpha = 0.30f),
                    scrimAlpha = 0.30f,
                )
            } else {
                MountainPalette(
                    far = lerp(background, primary, 0.20f),
                    mid = lerp(primary, Color.Black, 0.35f),
                    deep = lerp(primary, Color.Black, 0.88f),
                    mist = lerp(background, primary, 0.25f).copy(alpha = 0.45f),
                    scrimAlpha = 0.35f,
                )
            }
        }
    }
}

// ───────────────────────────── Capas ─────────────────────────────

/** base / amp / treeH son fracciones del alto. sharp: 0 lomas suaves .. 1 picos. freq: detalle. */
private data class LayerSpec(
    val seed: Int,
    val base: Float,
    val amp: Float,
    val sharp: Float,
    val freq: Float,
    val treeH: Float = 0f,
    val mist: Boolean = false,
)

private val LAYERS = listOf(
    LayerSpec(11, 0.36f, 0.13f, 0.30f, 1.6f),
    LayerSpec(23, 0.43f, 0.15f, 0.35f, 2.0f),
    LayerSpec(37, 0.50f, 0.17f, 0.30f, 2.4f),
    LayerSpec(41, 0.57f, 0.18f, 0.30f, 2.8f),
    LayerSpec(53, 0.64f, 0.20f, 0.25f, 3.2f, treeH = 0.030f),
    LayerSpec(67, 0.72f, 0.20f, 0.20f, 3.6f, treeH = 0.035f, mist = true),
    LayerSpec(71, 0.84f, 0.16f, 0.15f, 4.0f, treeH = 0.10f, mist = true),
    LayerSpec(89, 0.93f, 0.12f, 0.10f, 4.5f, treeH = 0.13f, mist = true),
    LayerSpec(97, 1.03f, 0.08f, 0.10f, 5.0f, treeH = 0.17f),
)

/** Cuánto se acerca el fondo de cada capa al color de la siguiente (suaviza las uniones). */
private const val BLEED_TO_NEXT = 0.6f

private class Band(val top: Float, val height: Float, val brush: Brush)

private class LayerShape(
    val fill: Path,
    val trees: Path?,
    val brush: Brush,
    val treeColor: Color,
    val band: Band?,
)

private class Scene(val layers: List<LayerShape>, val scrim: Brush) {
    fun draw(scope: DrawScope) = with(scope) {
        layers.forEach { l ->
            drawPath(l.fill, l.brush)
            l.trees?.let { drawPath(it, l.treeColor) }
            l.band?.let {
                drawRect(
                    brush = it.brush,
                    topLeft = Offset(0f, it.top),
                    size = Size(size.width, it.height),
                )
            }
        }
        // Oscurece progresivamente hacia abajo; arriba es 100% transparente.
        drawRect(scrim)
    }
}

// ───────────────────────────── Ruido y geometría ─────────────────────────────

private fun hash(i: Int, seed: Int): Float {
    var h = i * 374761393 + seed * 668265263
    h = (h xor (h ushr 13)) * 1274126177
    h = h xor (h ushr 16)
    return (h and 0xFFFF) / 65535f
}

private fun noise1(x: Float, seed: Int): Float {
    val i = floor(x).toInt()
    val f = x - i
    val u = f * f * (3f - 2f * f)
    return hash(i, seed) * (1f - u) + hash(i + 1, seed) * u
}

/** Ruido fractal (4 octavas) en 0..1. */
private fun fbm(x: Float, seed: Int): Float {
    var amp = 0.5f
    var freq = 1f
    var sum = 0f
    var norm = 0f
    for (o in 0 until 4) {
        sum += amp * noise1(x * freq, seed + o * 31)
        norm += amp
        amp *= 0.5f
        freq *= 2f
    }
    return sum / norm
}

/** Altura normalizada (0..1) de la cresta en x (0..1): mezcla de lomas y crestas filosas. */
private fun ridge(x: Float, spec: LayerSpec): Float {
    val n = fbm(x * spec.freq, spec.seed)
    val r = 1f - abs(2f * fbm(x * spec.freq * 0.8f + 10f, spec.seed + 7) - 1f)
    val m = n * (1f - spec.sharp) + r * spec.sharp
    return ((m - 0.5f) * 2f + 0.5f).coerceIn(0f, 1f)
}

/** Pino de varios pisos. Todos los subpaths giran igual -> se unen con fill NonZero. */
private fun Path.pine(cx: Float, baseY: Float, h: Float, widthK: Float, tiers: Int) {
    val top = baseY - h
    val half = h * 0.16f * widthK
    val step = h * 0.74f / tiers
    val tierH = h * 0.30f
    for (k in 0 until tiers) {
        val tt = top + step * k
        val tb = tt + tierH
        val hw = half * (0.4f + 0.6f * (k + 1) / tiers)
        moveTo(cx, tt)
        lineTo(cx + hw, tb)
        lineTo(cx - hw, tb)
        close()
    }
    val t = h * 0.015f
    val y0 = top + h * 0.80f
    moveTo(cx - t, y0)
    lineTo(cx + t, y0)
    lineTo(cx + t, baseY)
    lineTo(cx - t, baseY)
    close()
}

private fun buildScene(size: Size, palette: MountainPalette): Scene {
    val w = size.width
    val h = size.height
    val steps = 200
    val last = LAYERS.size - 1

    val layers = LAYERS.mapIndexed { index, spec ->
        val color = palette.depth(index / last.toFloat())
        val nextColor = palette.depth(minOf(index + 1, last) / last.toFloat())

        val ys = FloatArray(steps + 1) { s ->
            (spec.base - spec.amp * ridge(s / steps.toFloat(), spec)) * h
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
                    // racimos: zonas con árboles más altos y otras más bajos
                    val cluster = 0.65f + 0.35f * fbm(xi * 9f, spec.seed + 5)
                    val th = treeH * (0.5f + 0.7f * rnd.nextFloat()) * cluster * 1.3f
                    pine(
                        cx = x,
                        baseY = ridgeY + treeH * 0.25f,
                        h = th,
                        widthK = 0.8f + 0.4f * rnd.nextFloat(),
                        tiers = 4 + rnd.nextInt(3),
                    )
                    x += treeH * (0.14f + 0.20f * rnd.nextFloat())
                }
            }
        } else null

        // Con árboles: sólido hasta el valle más bajo (cuadra con las siluetas) y luego se
        // funde con la capa siguiente. Sin árboles: se funde desde la cresta.
        val startY = if (spec.treeH > 0f) ys.max() else ys.min()
        val bottom = if (index == last) color else lerp(color, nextColor, BLEED_TO_NEXT)
        val brush = Brush.verticalGradient(
            colors = listOf(color, bottom),
            startY = startY,
            endY = h,
        )

        val band = if (spec.mist) {
            val bandH = h * 0.16f
            val top = spec.base * h - bandH * 0.5f
            val clear = palette.mist.copy(alpha = 0f) // mismo tono, evita franjas oscuras
            Band(
                top = top,
                height = bandH,
                brush = Brush.verticalGradient(
                    colors = listOf(clear, palette.mist, clear),
                    startY = top,
                    endY = top + bandH,
                ),
            )
        } else null

        LayerShape(fill, trees, brush, color, band)
    }

    val deepClear = palette.deep.copy(alpha = 0f)
    val scrim = Brush.verticalGradient(
        0f to deepClear,
        0.45f to deepClear,
        1f to palette.deep.copy(alpha = palette.scrimAlpha),
        startY = 0f,
        endY = h,
    )
    return Scene(layers, scrim)
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