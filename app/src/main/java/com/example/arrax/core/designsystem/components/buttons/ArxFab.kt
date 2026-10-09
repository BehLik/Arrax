package com.example.arrax.core.designsystem.components.buttons

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

// Elevación mínima: separa el FAB del contenido sin sombras exageradas.
@Composable
private fun arxFabElevation() = FloatingActionButtonDefaults.elevation(
    defaultElevation = 2.dp,
    pressedElevation = 1.dp,
    focusedElevation = 2.dp,
    hoveredElevation = 2.dp
)

/** FAB redondo, una sola acción principal (ej. el [+] de la BottomBar). */
@Composable
fun ArxFab(
    icon: Painter,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = arxFabElevation()
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription
        )
    }
}

/** FAB extendido (icono + texto). Con [expanded] = false se contrae a solo icono al hacer scroll. */
@Composable
fun ArxExtendedFAB(
    text: String,
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = true
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        expanded = expanded,
        icon = {
            // Contraído, el texto desaparece: el icono pasa a ser el único descriptor.
            Icon(
                painter = icon,
                contentDescription = if (expanded) null else text
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge
            )
        },
        elevation = arxFabElevation()
    )
}

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

@Composable
private fun ArxFabsPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(ArxSpacing.xl),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.xl)
            ) {
                ArxFab(
                    icon = painterResource(id = ArxIcons.NfcOutline),
                    contentDescription = "Escanear",
                    onClick = {}
                )
                ArxExtendedFAB(
                    text = "Nuevo pedido",
                    icon = painterResource(id = ArxIcons.Add),
                    onClick = {}
                )
                ArxExtendedFAB(
                    text = "Nuevo pedido",
                    icon = painterResource(id = ArxIcons.Add),
                    onClick = {},
                    expanded = false
                )
            }
        }
    }
}

@Preview(name = "FABs – Light", showBackground = false)
@Composable
fun ArxFabsPreviewLight() = ArxFabsPreviewContent()

@Preview(
    name = "FABs – Dark",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ArxFabsPreviewDark() = ArxFabsPreviewContent()