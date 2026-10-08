package com.example.arrax.core.designsystem.components.backgrounds.blobs

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path

/**
 * Catálogo de formas orgánicas. Se definen en proporciones de ancho (w) y alto (h)
 * y se extienden fuera de pantalla para que el difuminado no deje cortes visibles.
 * [strength] multiplica el alpha base.
 */
enum class ArxShape(val strength: Float = 1f) {
    LeftBlob,
    RightBlob,
    TopLeftCorner,
    TopRightBlob,
    TopWave(0.8f),
    BottomWave,
    BottomHill(0.8f),
    BottomRightHill,
    BubbleTopRight(0.7f);

    fun path(w: Float, h: Float): Path = Path().apply {
        when (this@ArxShape) {
            LeftBlob -> {
                moveTo(-w * 0.10f, h * 0.14f)
                cubicTo(w * 0.20f, h * 0.12f, w * 0.30f, h * 0.30f, w * 0.16f, h * 0.42f)
                cubicTo(w * 0.08f, h * 0.50f, -w * 0.02f, h * 0.50f, -w * 0.10f, h * 0.48f)
                close()
            }
            RightBlob -> {
                moveTo(w * 1.10f, h * 0.30f)
                cubicTo(w * 0.80f, h * 0.28f, w * 0.70f, h * 0.46f, w * 0.84f, h * 0.58f)
                cubicTo(w * 0.92f, h * 0.65f, w * 1.02f, h * 0.66f, w * 1.10f, h * 0.64f)
                close()
            }
            TopLeftCorner -> {
                moveTo(-w * 0.10f, h * 0.20f)
                cubicTo(w * 0.20f, h * 0.22f, w * 0.42f, h * 0.10f, w * 0.50f, -h * 0.05f)
                lineTo(-w * 0.10f, -h * 0.05f)
                close()
            }
            TopRightBlob -> {
                moveTo(w * 0.40f, -h * 0.05f)
                cubicTo(w * 0.55f, h * 0.08f, w * 0.82f, h * 0.06f, w * 1.10f, h * 0.20f)
                lineTo(w * 1.10f, -h * 0.05f)
                close()
            }
            TopWave -> {
                moveTo(-w * 0.05f, -h * 0.05f)
                lineTo(-w * 0.05f, h * 0.12f)
                cubicTo(w * 0.30f, h * 0.18f, w * 0.62f, h * 0.04f, w * 1.05f, h * 0.11f)
                lineTo(w * 1.05f, -h * 0.05f)
                close()
            }
            BottomWave -> {
                moveTo(-w * 0.10f, h * 0.86f)
                cubicTo(w * 0.18f, h * 0.80f, w * 0.42f, h * 0.84f, w * 0.60f, h * 0.94f)
                cubicTo(w * 0.68f, h * 0.99f, w * 0.78f, h * 1.04f, w * 0.84f, h * 1.05f)
                lineTo(-w * 0.10f, h * 1.05f)
                close()
            }
            BottomHill -> {
                moveTo(-w * 0.05f, h * 0.93f)
                cubicTo(w * 0.25f, h * 0.84f, w * 0.62f, h * 1.00f, w * 1.05f, h * 0.88f)
                lineTo(w * 1.05f, h * 1.05f)
                lineTo(-w * 0.05f, h * 1.05f)
                close()
            }
            BottomRightHill -> {
                moveTo(w * 1.10f, h * 0.78f)
                cubicTo(w * 0.82f, h * 0.78f, w * 0.62f, h * 0.88f, w * 0.50f, h * 1.05f)
                lineTo(w * 1.10f, h * 1.05f)
                close()
            }
            BubbleTopRight -> {
                addOval(Rect(center = Offset(w * 0.98f, h * 0.06f), radius = w * 0.42f))
            }
        }
    }
}

/** Combinaciones listas para usar por tipo de pantalla. */
object ArxBackdrops {
    val Auth = listOf(ArxShape.LeftBlob)
    val AuthAlt = listOf(ArxShape.TopRightBlob, ArxShape.BottomWave)
    val Recovery = listOf(ArxShape.BottomWave)
    val Home = listOf(ArxShape.TopWave, ArxShape.BottomRightHill)
    val Detail = listOf(ArxShape.TopLeftCorner)
    val Lobby = listOf(ArxShape.BubbleTopRight, ArxShape.BottomHill)
}