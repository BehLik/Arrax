package com.example.arrax.core.designsystem.components.feedback

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxTheme

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
        shape = MaterialTheme.shapes.large, // Redondeo de 24dp
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,

        icon = if (icon != null) {
            {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
        } else null,

        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
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
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = confirmText.uppercase(),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        },

        dismissButton = if (dismissText != null) {
            {
                TextButton(
                    onClick = { onDismiss?.invoke() },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(
                        text = dismissText.uppercase(),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        } else null
    )
}

@Preview(showBackground = true, name = "Alert Dialog Dark")
@Composable
fun ArxAlertDialogPreview_Dark() {
    ArxTheme(darkTheme = true) {
        ArxAlertDialog(
            title = "Sincronización Completa",
            message = "Todos los registros offline han sido subidos a la nube exitosamente.",
            confirmText = "Entendido",
            onConfirm = {},
            icon = painterResource(id = ArxIcons.Sync) // Asegúrate de tener este ícono
        )
    }
}

@Preview(showBackground = true, name = "Alert Dialog Light")
@Composable
fun ArxAlertDialogPreview_Light() {
    ArxTheme {
        ArxAlertDialog(
            title = "Sincronización Completa",
            message = "Todos los registros offline han sido subidos a la nube exitosamente.",
            confirmText = "Entendido",
            onConfirm = {},
            icon = painterResource(id = ArxIcons.Sync)
        )
    }
}