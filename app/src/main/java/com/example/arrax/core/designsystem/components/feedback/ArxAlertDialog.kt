package com.example.arrax.core.designsystem.components.feedback

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.components.buttons.ArxTextButton
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxTheme

/**
 * Diálogo de confirmación / éxito.
 * - Éxito: icono check + un solo botón ("Continuar").
 * - Confirmación: icono de advertencia + Cancelar / Confirmar. [isDestructive] pinta la acción en error.
 */
@Composable
fun ArxAlertDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    dismissText: String? = null,
    onDismiss: (() -> Unit)? = null,
    icon: Painter? = null,
    isDestructive: Boolean = false
) {
    AlertDialog(
        onDismissRequest = { onDismiss?.invoke() },
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,

        icon = if (icon != null) {
            {
                Icon(
                    painter = icon,
                    contentDescription = null, // decorativo: el título ya lo describe
                    tint = if (isDestructive) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
        } else {
            null
        },

        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        },

        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        },

        confirmButton = {
            ArxTextButton(
                text = confirmText,
                onClick = onConfirm,
                contentColor = if (isDestructive) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
        },

        dismissButton = if (dismissText != null) {
            {
                ArxTextButton(
                    text = dismissText,
                    onClick = { onDismiss?.invoke() },
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            null
        }
    )
}

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

@Preview(name = "Dialog éxito – Dark", showBackground = false)
@Composable
fun ArxAlertDialogPreviewDark() {
    ArxTheme(darkTheme = true) {
        ArxAlertDialog(
            title = "Sincronización completa",
            message = "Todos los registros offline se subieron correctamente.",
            confirmText = "Continuar",
            onConfirm = {},
            icon = painterResource(id = ArxIcons.Sync)
        )
    }
}

@Preview(name = "Dialog éxito – Light", showBackground = false)
@Composable
fun ArxAlertDialogPreviewLight() {
    ArxTheme(darkTheme = false) {
        ArxAlertDialog(
            title = "Sincronización completa",
            message = "Todos los registros offline se subieron correctamente.",
            confirmText = "Continuar",
            onConfirm = {},
            icon = painterResource(id = ArxIcons.Sync)
        )
    }
}

@Preview(name = "Dialog confirmación destructiva – Light", showBackground = false)
@Composable
fun ArxAlertDialogDestructivePreview() {
    ArxTheme(darkTheme = false) {
        ArxAlertDialog(
            title = "¿Eliminar pedido?",
            message = "Esta acción no se puede deshacer.",
            confirmText = "Eliminar",
            onConfirm = {},
            dismissText = "Cancelar",
            onDismiss = {},
            isDestructive = true
        )
    }
}