package com.example.arrax.core.designsystem.components.buttons

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

// Alto cómodo de botón (touch target >= 48dp). Es una medida de componente, no un color.
private val ArxButtonHeight = 56.dp
private val ArxTouchTarget = 48.dp

/**
 * Contenido común de los botones: [spinner | leadingIcon] + texto + trailingIcon.
 * Es como el "molde" de un sándwich: cambian los panes (colores), pero el relleno es el mismo.
 */
@Composable
private fun RowScope.ArxButtonContent(
    text: String,
    contentColor: Color,
    loading: Boolean,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?
) {
    when {
        loading -> {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = contentColor
            )
            Spacer(Modifier.width(ArxSpacing.xs))
        }
        leadingIcon != null -> {
            leadingIcon()
            Spacer(Modifier.width(ArxSpacing.xs))
        }
    }
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge
    )
    if (trailingIcon != null) {
        Spacer(Modifier.width(ArxSpacing.xs))
        trailingIcon()
    }
}

/**
 * Botón principal. Sólido por defecto.
 * [hero] = true aplica el gradiente de marca (usar máximo UNO por pantalla, en el CTA principal).
 * [loading] = true muestra un spinner y bloquea el click sin cambiar los colores.
 */
@Composable
fun ArxPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    loading: Boolean = false,
    hero: Boolean = false
) {
    val shape = MaterialTheme.shapes.medium
    val useGradient = hero && enabled
    val contentColor =
        if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.extendedColors.disabled

    Button(
        onClick = { if (!loading) onClick() },
        enabled = enabled,
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (useGradient) Color.Transparent else MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.extendedColors.disabled
        ),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = ArxButtonHeight)
            .then(
                if (useGradient) {
                    Modifier.background(MaterialTheme.extendedColors.primaryGradient, shape)
                } else {
                    Modifier
                }
            )
    ) {
        ArxButtonContent(text, contentColor, loading, leadingIcon, trailingIcon)
    }
}

/** Botón secundario: contenedor primaryContainer + borde primary. */
@Composable
fun ArxSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    loading: Boolean = false
) {
    val contentColor =
        if (enabled) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.extendedColors.disabled

    OutlinedButton(
        onClick = { if (!loading) onClick() },
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.extendedColors.disabled
        ),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = ArxButtonHeight)
    ) {
        ArxButtonContent(text, contentColor, loading, leadingIcon, trailingIcon)
    }
}

/** Botón de texto: sin fondo, color primary. */
@Composable
fun ArxTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = MaterialTheme.colorScheme.primary
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.textButtonColors(
            contentColor = contentColor,
            disabledContentColor = MaterialTheme.extendedColors.disabled
        ),
        modifier = modifier.heightIn(min = ArxTouchTarget)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

/**
 * Botón de acción rápida de campo (icono grande + etiqueta).
 * [isHighlighted] = true resalta con primaryContainer; si no, usa superficie glass sutil.
 */
@Composable
fun ArxFieldActionButton(
    iconRes: Int,
    label: String,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false,
    onClick: () -> Unit
) {
    val containerColor = if (isHighlighted) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.extendedColors.glass
    }
    val contentColor = if (isHighlighted) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val borderColor = if (isHighlighted) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.extendedColors.glassBorder
    }

    Card(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = ArxSpacing.xl, horizontal = ArxSpacing.sm),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null, // la etiqueta de abajo ya describe la acción
                modifier = Modifier.size(36.dp),
                tint = contentColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = contentColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

@Composable
private fun ArxButtonsPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(ArxSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
            ) {
                ArxPrimaryButton(text = "Iniciar sesión", onClick = {}, hero = true)
                ArxPrimaryButton(
                    text = "Nuevo lote",
                    onClick = {},
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = ArxIcons.Camera),
                            contentDescription = null
                        )
                    }
                )
                ArxPrimaryButton(text = "Guardando", onClick = {}, loading = true)
                ArxPrimaryButton(text = "Deshabilitado", onClick = {}, enabled = false)
                ArxSecondaryButton(text = "Editar perfil", onClick = {})
                ArxSecondaryButton(text = "Deshabilitado", onClick = {}, enabled = false)
                ArxTextButton(text = "¿Olvidaste tu contraseña?", onClick = {})
            }
        }
    }
}

@Preview(name = "Botones – Light", showBackground = false)
@Composable
fun ArxButtonsPreviewLight() = ArxButtonsPreviewContent()

@Preview(
    name = "Botones – Dark",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ArxButtonsPreviewDark() = ArxButtonsPreviewContent()

@Composable
private fun ArxFieldActionButtonsPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Row(
                modifier = Modifier.padding(ArxSpacing.md),
                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)
            ) {
                ArxFieldActionButton(
                    iconRes = ArxIcons.NfcOutline,
                    label = "Escanear",
                    isHighlighted = true,
                    modifier = Modifier.weight(1f),
                    onClick = {}
                )
                ArxFieldActionButton(
                    iconRes = ArxIcons.Camera,
                    label = "Tomar\nfoto",
                    isHighlighted = false,
                    modifier = Modifier.weight(1f),
                    onClick = {}
                )
            }
        }
    }
}

@Preview(name = "Acciones de campo – Light", showBackground = false)
@Composable
fun ArxFieldActionButtonsPreviewLight() = ArxFieldActionButtonsPreviewContent()

@Preview(
    name = "Acciones de campo – Dark",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ArxFieldActionButtonsPreviewDark() = ArxFieldActionButtonsPreviewContent()