package com.example.arrax.core.designsystem.icons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
enum class ArxIconStyle { Tonal, Soft, Outlined, Solid }

enum class ArxIconShape { Rounded, Circle }

@Composable
fun ArxIcon(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current,
    size: Dp = 24.dp
) {
    Icon(
        painter = painterResource(icon),
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        tint = tint
    )
}


/**
 * Icono dentro de un contenedor del Design System.
 * Elige estilo y forma sin repetir Box + background en cada pantalla.
 */
@Composable
fun ArxIconBadge(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    style: ArxIconStyle = ArxIconStyle.Tonal,
    shape: ArxIconShape = ArxIconShape.Rounded,
    containerSize: Dp = ArxSpacing.huge,
    iconSize: Dp = 24.dp,
    contentDescription: String? = null,
    accent: Color? = null
) {
    val colors = MaterialTheme.colorScheme

    val containerColor: Color
    val contentColor: Color
    val border: BorderStroke?

    when (style) {
        ArxIconStyle.Tonal -> {
            containerColor = colors.primaryContainer
            contentColor = colors.onPrimaryContainer
            border = null
        }

        ArxIconStyle.Soft -> {
            val base = accent ?: colors.primary
            containerColor = base.copy(alpha = 0.15f)
            contentColor = base
            border = null
        }

        ArxIconStyle.Outlined -> {
            containerColor = Color.Transparent
            contentColor = colors.primary
            border = BorderStroke(1.dp, colors.outlineVariant)
        }

        ArxIconStyle.Solid -> {
            containerColor = colors.primary
            contentColor = colors.onPrimary
            border = null
        }
    }

    Surface(
        modifier = modifier.size(containerSize),
        shape = if (shape == ArxIconShape.Circle) CircleShape else MaterialTheme.shapes.medium,
        color = containerColor,
        contentColor = contentColor,
        border = border
    ) {
        androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
            ArxIcon(
                icon = icon,
                contentDescription = contentDescription,
                size = iconSize
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 393)
@Preview(showBackground = true, widthDp = 393, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ArxIconBadgePreview() {
    ArxTheme(darkTheme = androidx.compose.foundation.isSystemInDarkTheme()) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            Row(
                modifier = Modifier.padding(ArxSpacing.md),
                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)
            ) {
                ArxIconStyle.entries.forEach { style ->
                    ArxIconBadge(icon = ArxIcons.Event, style = style)
                }
                ArxIconBadge(icon = ArxIcons.User, style = ArxIconStyle.Tonal, shape = ArxIconShape.Circle)
                ArxIconBadge(icon = ArxIcons.User, style = ArxIconStyle.Solid, shape = ArxIconShape.Circle)
            }
        }
    }
}