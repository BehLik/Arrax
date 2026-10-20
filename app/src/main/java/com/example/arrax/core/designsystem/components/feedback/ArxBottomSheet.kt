package com.example.arrax.core.designsystem.components.feedback

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
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
 * Estructura: banda de encabezado (handle + icono + título + cerrar),
 * contenido con scroll y footer anclado. Todo con tokens del Design System.
 *
 * @param icon recurso drawable (usar ArxIcons.*), se muestra en un contenedor tonal.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArxBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    size: ArxBottomSheetSize = ArxBottomSheetSize.WrapContent,
    title: String? = null,
    subtitle: String? = null,
    @DrawableRes icon: Int? = null,
    showDragHandle: Boolean = true,
    showCloseButton: Boolean = false,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val colors = MaterialTheme.colorScheme
    val shapes = MaterialTheme.shapes
    val horizontal = ArxSpacing.xl

    val sheetHeight: Dp = when (size) {
        ArxBottomSheetSize.WrapContent -> Dp.Unspecified
        ArxBottomSheetSize.Compact -> screenHeight * 0.42f
        ArxBottomSheetSize.Expanded -> screenHeight * 0.78f
        ArxBottomSheetSize.Full -> screenHeight * 0.96f
    }

    val baseShape = when (size) {
        ArxBottomSheetSize.WrapContent,
        ArxBottomSheetSize.Compact -> shapes.large

        ArxBottomSheetSize.Expanded,
        ArxBottomSheetSize.Full -> shapes.extraLarge
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val hasHeader = title != null || subtitle != null || icon != null

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = baseShape.copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        ),
        containerColor = colors.surface,
        contentColor = colors.onSurface,
        scrimColor = colors.scrim.copy(alpha = 0.48f),
        tonalElevation = 0.dp,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (size == ArxBottomSheetSize.WrapContent)
                        Modifier.heightIn(max = screenHeight * 0.85f)
                    else Modifier.height(sheetHeight)
                )
        ) {
            // ── Banda de encabezado (handle + header) ──
            if (showDragHandle || hasHeader) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.surfaceContainerHigh)
                ) {
                    if (showDragHandle) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = ArxSpacing.sm, bottom = ArxSpacing.xs),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(ArxSpacing.huge)
                                    .height(ArxSpacing.xxs)
                                    .background(
                                        color = colors.primary.copy(alpha = 0.5f),
                                        shape = CircleShape
                                    )
                            )
                        }
                    }

                    if (hasHeader) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = horizontal,
                                    end = ArxSpacing.sm,
                                    bottom = ArxSpacing.md
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
                        ) {
                            icon?.let {
                                Box(
                                    modifier = Modifier
                                        .size(ArxSpacing.huge)
                                        .background(colors.primaryContainer, shapes.medium),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(it),
                                        contentDescription = null,
                                        tint = colors.onPrimaryContainer
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                title?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = colors.onSurface
                                    )
                                }
                                subtitle?.let {
                                    if (title != null) Spacer(Modifier.height(ArxSpacing.xxs))
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = colors.onSurfaceVariant
                                    )
                                }
                            }

                            if (showCloseButton) {
                                IconButton(onClick = onDismissRequest) {
                                    Icon(
                                        painter = painterResource(ArxIcons.Close),
                                        contentDescription = "Cerrar",
                                        tint = colors.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(color = colors.outlineVariant)
            }

            // ── Contenido con scroll ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = size != ArxBottomSheetSize.WrapContent)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontal, vertical = ArxSpacing.md),
                content = content
            )

            // ── Footer anclado a sangre completa ──
            if (footer != null) {
                HorizontalDivider(color = colors.outlineVariant)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.surfaceContainerLow)
                        .padding(horizontal = horizontal, vertical = ArxSpacing.md)
                ) {
                    footer()
                }
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
    ArxTheme(darkTheme = false) {
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
            icon = when (size) {
                ArxBottomSheetSize.WrapContent -> ArxIcons.Add
                ArxBottomSheetSize.Compact -> ArxIcons.Settings
                ArxBottomSheetSize.Expanded -> ArxIcons.Event
                ArxBottomSheetSize.Full -> ArxIcons.TeamOutline
            },
            showDragHandle = true,
            showCloseButton = size == ArxBottomSheetSize.Full,
            footer = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
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
                    Spacer(Modifier.height(ArxSpacing.sm))

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

                    Spacer(Modifier.height(ArxSpacing.xs))

                    OutlinedTextField(
                        value = "Equipo Arrax",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Nombre") }
                    )

                    Spacer(Modifier.height(ArxSpacing.md))

                    OutlinedTextField(
                        value = "Desarrollo y colaboración",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        label = { Text("Descripción") }
                    )

                    Spacer(Modifier.height(ArxSpacing.md))

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

                    Spacer(Modifier.height(ArxSpacing.sm))

                    OutlinedTextField(
                        value = "Gestiona proyectos y eventos.",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        label = { Text("Descripción") }
                    )

                    Spacer(Modifier.height(ArxSpacing.xl))

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
            .padding(vertical = ArxSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
    ) {
        Box(
            modifier = Modifier
                .size(ArxSpacing.huge)
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
    heightDp = 700,
    uiMode = Configuration.UI_MODE_NIGHT_NO
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