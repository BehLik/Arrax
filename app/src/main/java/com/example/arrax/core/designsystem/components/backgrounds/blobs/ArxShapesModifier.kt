package com.example.arrax.core.designsystem.components.backgrounds.blobs

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.luminance
import kotlin.math.abs
import kotlin.math.hypot

/**
 * Dibuja [shapes] con borde difuminado en dos capas (halo amplio + núcleo).
 * [feather]: 0.2 = borde más definido .. 1.0 = se difumina desde el mismo borde de pantalla.
 * Path y Brush se construyen una sola vez por tamaño (drawWithCache).
 */
@Composable
fun Modifier.arxShapes(shapes: List<ArxShape>, feather: Float = 0.85f): Modifier {
    if (shapes.isEmpty()) return this
    val colors = MaterialTheme.colorScheme
    val baseAlpha = if (colors.background.luminance() < 0.5f) 0.11f else 0.08f
    val primary = colors.primary
    val fadeStart = 1f - feather.coerceIn(0.2f, 1f)

    return this.then(
        Modifier.drawWithCache {
            val w = size.width
            val h = size.height

            val layers = shapes.flatMap { s ->
                val path = s.path(w, h)
                val b = path.getBounds()

                val touchL = b.left <= 1f
                val touchR = b.right >= w - 1f
                val touchT = b.top <= 1f
                val touchB = b.bottom >= h - 1f

                val l = b.left.coerceAtLeast(0f)
                val r = b.right.coerceAtMost(w)
                val t = b.top.coerceAtLeast(0f)
                val bt = b.bottom.coerceAtMost(h)

                val cx = when {
                    touchL && touchR -> w / 2f
                    touchL -> 0f
                    touchR -> w
                    else -> (l + r) / 2f
                }
                val cy = when {
                    touchT && touchB -> h / 2f
                    touchT -> 0f
                    touchB -> h
                    else -> (t + bt) / 2f
                }
                val maxR = hypot(
                    maxOf(abs(cx - l), abs(cx - r)),
                    maxOf(abs(cy - t), abs(cy - bt)),
                ).coerceAtLeast(1f)
                val vertical = touchL && touchR && (touchT || touchB)

                fun brush(alpha: Float, reach: Float): Brush {
                    val stops = easedStops(primary.copy(alpha = alpha * s.strength), fadeStart)
                    return if (vertical) {
                        val span = (bt - t) * reach
                        Brush.verticalGradient(
                            *stops,
                            startY = if (touchT) 0f else h,
                            endY = if (touchT) span else h - span,
                        )
                    } else {
                        Brush.radialGradient(*stops, center = Offset(cx, cy), radius = maxR * reach)
                    }
                }

                listOf(
                    path to brush(baseAlpha * 0.55f, 0.95f), // halo amplio y muy tenue
                    path to brush(baseAlpha, 0.60f),         // núcleo
                )
            }

            onDrawBehind {
                layers.forEach { (path, brush) -> drawPath(path, brush) }
            }
        }
    )
}

/** Caída suave (smoothstep): alpha pleno -> 0 sin franjas ni borde duro. */
private fun easedStops(color: Color, fadeStart: Float): Array<Pair<Float, Color>> {
    val n = 6
    return Array(n + 1) { i ->
        val t = i / n.toFloat()
        val ease = t * t * (3f - 2f * t)
        (fadeStart + (1f - fadeStart) * t) to color.copy(alpha = color.alpha * (1f - ease))
    }
}