package com.example.arrax.core.designsystem.components.brand

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

enum class ArxLogoLayout {
    /** Login / splash: isotipo en contenedor arriba, nombre y tagline centrados debajo. */
    Stacked,

    /** Headers / banners: isotipo a la izquierda, nombre + tagline a la derecha. */
    Horizontal,
}

object ArxLogoDefaults {
    const val Tagline = "Control. Contexto. Acción."

    /** Alto del wordmark relativo a [markSize], medido del mockup. */
    const val StackedWordmarkRatio = 0.305f
    const val HorizontalWordmarkRatio = 0.56f
}

/** Texto "Control. Contexto. Acción." (3er elemento del branding). */
@Composable
fun ArxTagline(
    modifier: Modifier = Modifier,
    text: String = ArxLogoDefaults.Tagline,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    style: TextStyle = MaterialTheme.typography.labelMedium,
    textAlign: TextAlign = TextAlign.Center,
) {
    Text(text = text, modifier = modifier, color = color, style = style, textAlign = textAlign)
}

/**
 * Logo completo = [ArxLogoMark] + [ArxWordmark] + [ArxTagline], cada pieza apagable.
 *
 * @param markSize       tamaño base; el wordmark y el tagline se escalan a partir de él.
 * @param withContainer  contenedor oscuro alrededor del isotipo (Stacked sí, Horizontal no).
 * @param markOuterColor color del chevron exterior cuando no hay contenedor. Por defecto es
 *                       oscuro en Light y blanco en Dark (ver [ArxLogoMarkDefaults.outerColor]).
 *
 * Para un banner siempre oscuro en modo Light, envuélvelo en `ArxTheme(darkTheme = true) { … }`.
 */
@Composable
fun ArxLogo(
    modifier: Modifier = Modifier,
    layout: ArxLogoLayout = ArxLogoLayout.Stacked,
    markSize: Dp = 96.dp,
    withContainer: Boolean = layout == ArxLogoLayout.Stacked,
    showMark: Boolean = true,
    showWordmark: Boolean = true,
    showTagline: Boolean = true,
    tagline: String = ArxLogoDefaults.Tagline,
    wordmarkColor: Color = MaterialTheme.colorScheme.onBackground,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    taglineColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    glow: Float = ArxLogoMarkDefaults.glow(),
    markOuterColor: Color = ArxLogoMarkDefaults.outerColor(),
) {
    val stackedGap = ArxSpacing.sm
    val horizontalGap = ArxSpacing.md
    val textGap = ArxSpacing.xs
    val taglineStyle = MaterialTheme.typography.labelMedium.copy(
        fontSize = (markSize.value * 0.13f).coerceIn(10f, 16f).sp
    )
    val description = if (showTagline) "ARRAX. $tagline" else "ARRAX"

    val mark: @Composable () -> Unit = {
        ArxLogoMark(
            size = markSize,
            withContainer = withContainer,
            glow = glow,
            outerColor = markOuterColor,
        )
    }

    when (layout) {
        ArxLogoLayout.Stacked -> Column(
            modifier = modifier.clearAndSetSemantics { contentDescription = description },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(stackedGap),
        ) {
            if (showMark) mark()
            if (showWordmark) {
                ArxWordmark(
                    height = markSize * ArxLogoDefaults.StackedWordmarkRatio,
                    textColor = wordmarkColor,
                    accentColor = accentColor,
                )
            }
            if (showTagline) {
                ArxTagline(text = tagline, color = taglineColor, style = taglineStyle)
            }
        }

        ArxLogoLayout.Horizontal -> Row(
            modifier = modifier.clearAndSetSemantics { contentDescription = description },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(horizontalGap),
        ) {
            if (showMark) mark()
            if (showWordmark || showTagline) {
                Column(verticalArrangement = Arrangement.spacedBy(textGap)) {
                    if (showWordmark) {
                        ArxWordmark(
                            height = markSize * ArxLogoDefaults.HorizontalWordmarkRatio,
                            textColor = wordmarkColor,
                            accentColor = accentColor,
                        )
                    }
                    if (showTagline) {
                        ArxTagline(
                            text = tagline,
                            color = taglineColor,
                            style = taglineStyle,
                            textAlign = TextAlign.Start,
                        )
                    }
                }
            }
        }
    }
}

// ───────────────────────────── Previews ─────────────────────────────

@Preview(name = "Logo Stacked - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ArxLogoPreviewStackedLight() {
    ArxTheme {
        ArxBackground(decoration = ArxDecoration.None) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { ArxLogo() }
        }
    }
}

@Preview(name = "Logo Stacked - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ArxLogoPreviewStackedDark() {
    ArxTheme(darkTheme = true) {
        ArxBackground(decoration = ArxDecoration.None) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { ArxLogo() }
        }
    }
}

@Preview(name = "Logo Horizontal - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ArxLogoPreviewHorizontalDark() {
    ArxTheme(darkTheme = true) {
        ArxBackground(decoration = ArxDecoration.Mountains) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.xxl, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ArxLogo(layout = ArxLogoLayout.Horizontal, markSize = 64.dp)
                // Variante compacta tipo top bar del Lobby: sin tagline.
                //ArxLogo(layout = ArxLogoLayout.Horizontal, markSize = 28.dp, showTagline = false)
            }
        }
    }
}

@Preview(name = "Logo Horizontal - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ArxLogoPreviewHorizontalLight() {
    ArxTheme(darkTheme = false) {
        ArxBackground(decoration = ArxDecoration.Mountains) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.xxl, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ArxLogo(layout = ArxLogoLayout.Horizontal, markSize = 64.dp)
            }
        }
    }
}