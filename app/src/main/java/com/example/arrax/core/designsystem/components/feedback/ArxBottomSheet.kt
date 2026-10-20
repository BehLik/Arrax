
package com.example.arrax.core.designsystem.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import com.example.arrax.core.designsystem.theme.ArxTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.theme.ArxTheme

/**
 * Tamaños disponibles para los Bottom Sheets de Arrax.
 */
enum class ArxBottomSheetSize {
    WrapContent,
    Compact,
    Expanded,
    Full
}

/**
 * Contenedor modal reutilizable de Arrax.
 *
 * Conserva la identidad visual del Design System y permite
 * personalizar tamaño, encabezado, contenido y acciones inferiores.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArxBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    size: ArxBottomSheetSize = ArxBottomSheetSize.WrapContent,
    title: String? = null,
    subtitle: String? = null,
    showDragHandle: Boolean = true,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val screenHeight =
        LocalConfiguration.current.screenHeightDp.dp

    val sheetHeight: Dp = when (size) {
        ArxBottomSheetSize.WrapContent -> Dp.Unspecified
        ArxBottomSheetSize.Compact -> screenHeight * 0.42f
        ArxBottomSheetSize.Expanded -> screenHeight * 0.78f
        ArxBottomSheetSize.Full -> screenHeight * 0.96f
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        scrimColor = MaterialTheme.colorScheme.scrim.copy(
            alpha = 0.48f
        ),
        tonalElevation = 0.dp,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    when (size) {
                        ArxBottomSheetSize.WrapContent ->
                            Modifier.heightIn(
                                max = screenHeight * 0.85f
                            )

                        else -> Modifier.height(sheetHeight)
                    }
                )
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp)
        ) {
            if (showDragHandle) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.10f)
                            .height(4.dp)
                            .background(
                                color = MaterialTheme.colorScheme
                                    .onSurfaceVariant.copy(alpha = 0.45f),
                                shape = CircleShape
                            )
                    )
                }
            }

            if (title != null || subtitle != null) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    title?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    subtitle?.let {
                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                        .copy(alpha = 0.65f)
                )

                Spacer(Modifier.height(16.dp))
            }

            if (size == ArxBottomSheetSize.WrapContent) {
                content()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    content()
                }
            }

            if (footer != null) {
                Spacer(Modifier.height(16.dp))
                footer()
            }
        }
    }
}

/* ─────────────────────────────────────────────
 * PREVIEWS — ARX BOTTOM SHEET
 * ───────────────────────────────────────────── */
@Composable
private fun ArxBottomSheetPreviewContent(
    size: ArxBottomSheetSize
) {
    ArxTheme(darkTheme = true) {
        ArxBottomSheet(
            onDismissRequest = {},
            size = size,
            title = when (size) {
                ArxBottomSheetSize.WrapContent -> "Acciones rápidas"
                ArxBottomSheetSize.Compact -> "Opciones del negocio"
                ArxBottomSheetSize.Expanded -> "Crear negocio"
                ArxBottomSheetSize.Full -> "Configurar negocio"
            },
            subtitle = when (size) {
                ArxBottomSheetSize.WrapContent ->
                    "Elige una acción para continuar"

                ArxBottomSheetSize.Compact ->
                    "Gestiona tu espacio de trabajo"

                ArxBottomSheetSize.Expanded ->
                    "Define los datos de tu espacio"

                ArxBottomSheetSize.Full ->
                    "Personaliza tu espacio de trabajo"
            },
            showDragHandle = true,
            footer = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Continuar")
                    }

                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        ) {
            when (size) {
                ArxBottomSheetSize.WrapContent -> {
                    Text("¿Qué deseas hacer?")
                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Crear negocio")
                    }

                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Unirme a un negocio")
                    }
                }

                ArxBottomSheetSize.Compact -> {
                    PreviewOption("Editar información")
                    PreviewOption("Gestionar miembros")
                    PreviewOption("Compartir invitación")
                }

                ArxBottomSheetSize.Expanded -> {
                    Text(
                        "Nombre del negocio",
                        style = MaterialTheme.typography.labelLarge
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = "Equipo Arrax",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Nombre") }
                    )

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = "Desarrollo y colaboración",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        label = { Text("Descripción") }
                    )

                    Spacer(Modifier.height(16.dp))

                    PreviewOption("Logotipo del negocio")
                    PreviewOption("Invitar miembros")
                }

                ArxBottomSheetSize.Full -> {
                    Text(
                        "Información general",
                        style = MaterialTheme.typography.titleSmall
                    )

                    OutlinedTextField(
                        value = "Equipo Arrax",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nombre") }
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = "Gestiona proyectos y eventos.",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        label = { Text("Descripción") }
                    )

                    Spacer(Modifier.height(24.dp))

                    Text(
                        "Preferencias",
                        style = MaterialTheme.typography.titleMedium
                    )

                    PreviewOption("Notificaciones del negocio")
                    PreviewOption("Permitir invitaciones")
                    PreviewOption("Mostrar actividad reciente")
                    PreviewOption("Sincronización automática")
                }
            }
        }
    }
}

@Composable
private fun PreviewOption(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text.take(1),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
    )
}

@Preview(
    name = "Bottom Sheet - Wrap Content",
    showBackground = true,
    widthDp = 393,
    heightDp = 700
)
@Composable
private fun ArxBottomSheetWrapContentPreview() {
    ArxBottomSheetPreviewContent(ArxBottomSheetSize.WrapContent)
}

@Preview(
    name = "Bottom Sheet - Compact",
    showBackground = true,
    widthDp = 393,
    heightDp = 700
)
@Composable
private fun ArxBottomSheetCompactPreview() {
    ArxBottomSheetPreviewContent(ArxBottomSheetSize.Compact)
}

@Preview(
    name = "Bottom Sheet - Expanded",
    showBackground = true,
    widthDp = 393,
    heightDp = 800
)
@Composable
private fun ArxBottomSheetExpandedPreview() {
    ArxBottomSheetPreviewContent(ArxBottomSheetSize.Expanded)
}

@Preview(
    name = "Bottom Sheet - Full",
    showBackground = true,
    widthDp = 393,
    heightDp = 900
)
@Composable
private fun ArxBottomSheetFullPreview() {
    ArxBottomSheetPreviewContent(ArxBottomSheetSize.Full)
}
