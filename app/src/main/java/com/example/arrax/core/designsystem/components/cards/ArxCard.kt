package com.example.arrax.core.designsystem.components.cards

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

/** Variantes visuales de [ArxCard]. Glass es un efecto, no otra tarjeta. */
enum class ArxCardVariant { Standard, Glass }

/**
 * Tarjeta base de Arrax.
 * - Standard: superficie sólida con elevación suave.
 * - Glass: superficie translúcida + borde sutil (tokens extendedColors.glass / glassBorder).
 *
 * [colors] y [elevation] ahora son opcionales: si no los pasas, los decide la [variant].
 * Quien ya los pasaba sigue funcionando igual.
 */
@Composable
fun ArxCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    colors: CardColors? = null,
    elevation: CardElevation? = null,
    variant: ArxCardVariant = ArxCardVariant.Standard,
    accentColor: Color? = null,   // tiñe fondo y borde (alertas, banners)
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = MaterialTheme.shapes.medium
    val isGlass = variant == ArxCardVariant.Glass

    val containerColor = when {
        accentColor != null -> accentColor.copy(alpha = 0.12f)
        isGlass -> MaterialTheme.extendedColors.glass
        else -> MaterialTheme.colorScheme.surface
    }
    val resolvedColors = colors ?: CardDefaults.cardColors(
        containerColor = containerColor,
        contentColor = MaterialTheme.colorScheme.onSurface
    )
    val resolvedElevation = elevation ?: CardDefaults.cardElevation(
        defaultElevation = if (isGlass || accentColor != null) 0.dp else 2.dp
    )
    val border = when {
        accentColor != null -> BorderStroke(1.dp, accentColor.copy(alpha = 0.5f))
        isGlass -> BorderStroke(1.dp, MaterialTheme.extendedColors.glassBorder)
        else -> null
    }

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            colors = resolvedColors,
            elevation = resolvedElevation,
            border = border,
            content = content
        )
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            colors = resolvedColors,
            elevation = resolvedElevation,
            border = border,
            content = content
        )
    }
}

/**
 * Obsoleto: usa `ArxCard(variant = ArxCardVariant.Glass)`.
 * Se mantiene para que las llamadas existentes compilen. [baseColor] y [borderColor]
 * ya no se usan: el glass sale de los tokens del tema (y se adapta solo a Light/Dark).
 */
@Deprecated(
    message = "Usa ArxCard con variant = ArxCardVariant.Glass",
    replaceWith = ReplaceWith(
        "ArxCard(modifier = modifier, onClick = onClick, variant = ArxCardVariant.Glass, content = content)"
    )
)
@Composable
fun ArxGlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    baseColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    content: @Composable ColumnScope.() -> Unit
) {
    ArxCard(
        modifier = modifier,
        onClick = onClick,
        variant = ArxCardVariant.Glass,
        content = content
    )
}

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

@Composable
private fun ArxCardsPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(ArxSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)) {
                    ArxMetricCard(
                        title = "Pedidos",
                        value = "24",
                        subtitle = "+5% esta semana",
                        iconRes = ArxIcons.Camera,
                        modifier = Modifier.weight(1f),
                        isHighlighted = true
                    )
                    ArxMetricCard(
                        title = "Pendientes",
                        value = "6",
                        iconRes = ArxIcons.NfcOutline,
                        modifier = Modifier.weight(1f)
                    )
                }
                ArxActionCard(
                    title = "Registrar nuevo pedido",
                    description = "Anota el corte, el precio por kilo y el peso a entregar.",
                    iconRes = ArxIcons.Camera,
                    onClick = {}
                )
            }
        }
    }
}

@Preview(name = "Cards – Light", showBackground = false)
@Composable
fun ArxCardsPreviewLight() = ArxCardsPreviewContent()

@Preview(
    name = "Cards – Dark",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ArxCardsPreviewDark() = ArxCardsPreviewContent()