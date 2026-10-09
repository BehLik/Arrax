package com.example.arrax.core.designsystem.components.feedback

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

/** Estado semántico del chip. Neutral = sin significado de estado (outlined / surfaceVariant). */
enum class ArxChipStatus { Neutral, Success, Warning, Info, Error }

/**
 * Chip de estado.
 *
 * Dos formas de usarlo:
 * 1. Semántica (recomendada): `ArxStatusChip(text = "Pesado", status = ArxChipStatus.Success)`.
 *    Muestra un punto del color del estado + borde del estado + texto legible (onSurface).
 *    Así el estado nunca depende solo del color: siempre va acompañado del texto.
 * 2. Colores explícitos (compatibilidad): pasa [containerColor] y [contentColor] como antes.
 *un paso mas... :/
 */
@Composable
fun ArxStatusChip(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    iconRes: Int? = null,
    isOutlined: Boolean = false,
    status: ArxChipStatus = ArxChipStatus.Neutral
) {
    val shape = MaterialTheme.shapes.small
    val explicitColors = containerColor != Color.Unspecified && contentColor != Color.Unspecified
    val statusColor: Color? = when (status) {
        ArxChipStatus.Success -> MaterialTheme.extendedColors.success.color
        ArxChipStatus.Warning -> MaterialTheme.extendedColors.warning.color
        ArxChipStatus.Info -> MaterialTheme.extendedColors.info.color
        ArxChipStatus.Error -> MaterialTheme.colorScheme.error
        ArxChipStatus.Neutral -> null
    }

    val resolvedContainer: Color
    val resolvedContent: Color
    val resolvedBorder: Color?
    when {
        explicitColors -> {
            resolvedContainer = if (isOutlined) Color.Transparent else containerColor
            resolvedContent = contentColor
            resolvedBorder = if (isOutlined) contentColor else null
        }
        statusColor != null -> {
            resolvedContainer = Color.Transparent
            resolvedContent = MaterialTheme.colorScheme.onSurface
            resolvedBorder = statusColor
        }
        else -> {
            resolvedContainer = if (isOutlined) Color.Transparent else MaterialTheme.colorScheme.surfaceVariant
            resolvedContent = MaterialTheme.colorScheme.onSurfaceVariant
            resolvedBorder = if (isOutlined) MaterialTheme.colorScheme.outline else null
        }
    }

    Row(
        modifier = modifier
            .clip(shape)
            .background(resolvedContainer)
            .then(
                if (resolvedBorder != null) Modifier.border(1.dp, resolvedBorder, shape) else Modifier
            )
            .padding(horizontal = ArxSpacing.sm, vertical = ArxSpacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (statusColor != null && !explicitColors) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(statusColor)
            )
            Spacer(modifier = Modifier.width(ArxSpacing.xs))
        }
        if (iconRes != null) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = resolvedContent,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(ArxSpacing.xxs))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = resolvedContent
        )
    }
}

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

@Composable
private fun ArxStatusChipPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Row(
                modifier = Modifier.padding(ArxSpacing.md),
                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
            ) {
                ArxStatusChip(text = "Pesado", status = ArxChipStatus.Success)
                ArxStatusChip(text = "Pendiente", status = ArxChipStatus.Warning)
                ArxStatusChip(text = "En proceso", status = ArxChipStatus.Info)
                ArxStatusChip(text = "Error", status = ArxChipStatus.Error)
            }
        }
    }
}

@Composable
private fun ArxStatusChipExtrasPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Row(
                modifier = Modifier.padding(ArxSpacing.md),
                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
            ) {
                ArxStatusChip(text = "Neutral")
                ArxStatusChip(text = "Neutral outlined", isOutlined = true)
                ArxStatusChip(
                    text = "Con icono",
                    iconRes = ArxIcons.Check,
                    status = ArxChipStatus.Success
                )
            }
        }
    }
}

@Preview(name = "Chips – Light", showBackground = false)
@Composable
fun ArxStatusChipPreviewLight() = ArxStatusChipPreviewContent()

@Preview(
    name = "Chips – Dark",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ArxStatusChipPreviewDark() = ArxStatusChipPreviewContent()

@Preview(name = "Chips neutrales – Light", showBackground = false)
@Composable
fun ArxStatusChipExtrasPreviewLight() = ArxStatusChipExtrasPreviewContent()