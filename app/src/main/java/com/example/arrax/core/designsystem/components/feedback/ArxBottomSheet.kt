package com.example.arrax.core.designsystem.components.feedback

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

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
    val colors = MaterialTheme.colorScheme
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = arxSheetShape(size),
        containerColor = colors.surface,
        contentColor = colors.onSurface,
        scrimColor = colors.scrim.copy(alpha = 0.48f),
        tonalElevation = 0.dp,
        dragHandle = null
    ) {
        ArxBottomSheetContent(
            size = size,
            screenHeight = LocalConfiguration.current.screenHeightDp.dp,
            title = title,
            subtitle = subtitle,
            icon = icon,
            showDragHandle = showDragHandle,
            showCloseButton = showCloseButton,
            onClose = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) onDismissRequest()
                }
            },
            footer = footer,
            content = content
        )
    }
}

/* ─────────────────────────────────────────────
 * INTERNOS
 * ───────────────────────────────────────────── */

/** Esquinas superiores según tamaño; abajo siempre recto (pegado al borde). */
@Composable
private fun arxSheetShape(size: ArxBottomSheetSize): CornerBasedShape {
    val shapes = MaterialTheme.shapes
    val base = when (size) {
        ArxBottomSheetSize.WrapContent,
        ArxBottomSheetSize.Compact -> shapes.large

        ArxBottomSheetSize.Expanded,
        ArxBottomSheetSize.Full -> shapes.extraLarge
    }
    return base.copy(
        bottomStart = CornerSize(0.dp),
        bottomEnd = CornerSize(0.dp)
    )
}

/**
 * Cuerpo del sheet, separado del ModalBottomSheet para poder
 * previsualizarlo de forma fiable en Android Studio.
 */
@Composable
private fun ArxBottomSheetContent(
    size: ArxBottomSheetSize,
    screenHeight: Dp,
    title: String?,
    subtitle: String?,
    @DrawableRes icon: Int?,
    showDragHandle: Boolean,
    showCloseButton: Boolean,
    onClose: () -> Unit,
    footer: (@Composable () -> Unit)?,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val shapes = MaterialTheme.shapes
    val horizontal = ArxSpacing.xl
    val hasHeader = title != null || subtitle != null || icon != null

    val sheetHeight: Dp = when (size) {
        ArxBottomSheetSize.WrapContent -> Dp.Unspecified
        ArxBottomSheetSize.Compact -> screenHeight * 0.42f
        ArxBottomSheetSize.Expanded -> screenHeight * 0.78f
        ArxBottomSheetSize.Full -> screenHeight * 0.96f
    }

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
                            FilledTonalIconButton(
                                onClick = onClose,
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = colors.surfaceContainerHighest,
                                    contentColor = colors.onSurfaceVariant
                                )
                            ) {
                                Icon(
                                    painter = painterResource(ArxIcons.Close),
                                    contentDescription = "Cerrar"
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

/* ─────────────────────────────────────────────
 * PREVIEWS — ARX BOTTOM SHEET
 *
 * Cada preview simula una pantalla real: fondo + scrim + sheet,
 * en un lienzo fijo de 393x800 para comparar proporciones.
 * Claro y oscuro se generan con un solo @ArxSheetPreviews.
 * ───────────────────────────────────────────── */

@Preview(name = "Claro", showBackground = true, widthDp = 393, heightDp = 800)
@Preview(
    name = "Oscuro",
    showBackground = true,
    widthDp = 393,
    heightDp = 800,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
private annotation class ArxSheetPreviews

@Composable
private fun SheetPreviewFrame(
    size: ArxBottomSheetSize,
    label: String,
    title: String? = null,
    subtitle: String? = null,
    @DrawableRes icon: Int? = null,
    showCloseButton: Boolean = false,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    ArxTheme(darkTheme = isSystemInDarkTheme()) {
        val colors = MaterialTheme.colorScheme

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
        ) {
            // Pantalla simulada detrás del sheet
            Column(
                modifier = Modifier.padding(ArxSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.onSurfaceVariant
                )
                repeat(4) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(ArxSpacing.giant)
                            .background(colors.surfaceContainer, MaterialTheme.shapes.medium)
                    )
                }
            }

            // Scrim
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.scrim.copy(alpha = 0.48f))
            )

            // Sheet
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                color = colors.surface,
                contentColor = colors.onSurface,
                shape = arxSheetShape(size)
            ) {
                ArxBottomSheetContent(
                    size = size,
                    screenHeight = maxHeight,
                    title = title,
                    subtitle = subtitle,
                    icon = icon,
                    showDragHandle = true,
                    showCloseButton = showCloseButton,
                    onClose = {},
                    footer = footer,
                    content = content
                )
            }
        }
    }
}

/* ── Piezas de muestra ── */

@Composable
private fun PreviewOption(text: String, @DrawableRes icon: Int) {
    val colors = MaterialTheme.colorScheme

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
                .background(colors.primary.copy(alpha = 0.15f), MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = colors.primary
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(ArxIcons.Forward),
            contentDescription = null,
            tint = colors.onSurfaceVariant
        )
    }

    HorizontalDivider(color = colors.outlineVariant.copy(alpha = 0.4f))
}

@Composable
private fun PreviewFooterRow(primary: String, secondary: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)) {
        OutlinedButton(onClick = {}, modifier = Modifier.weight(1f)) { Text(secondary) }
        Button(onClick = {}, modifier = Modifier.weight(1f)) { Text(primary) }
    }
}

@Composable
private fun PreviewFooterStacked(primary: String, secondary: String) {
    Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text(primary) }
        OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text(secondary) }
    }
}

/* ── Variantes ── */

@ArxSheetPreviews
@Composable
private fun PreviewWrapContent() {
    SheetPreviewFrame(
        size = ArxBottomSheetSize.WrapContent,
        label = "WrapContent · altura según contenido (máx. 85 %)",
        title = "Acciones rápidas",
        subtitle = "Elige una acción para continuar",
        icon = ArxIcons.Add
    ) {
        PreviewOption("Crear negocio", ArxIcons.Add)
        PreviewOption("Unirme con código", ArxIcons.GroupAdd)
        PreviewOption("Escanear QR", ArxIcons.ScanQr)
    }
}

@ArxSheetPreviews
@Composable
private fun PreviewCompact() {
    SheetPreviewFrame(
        size = ArxBottomSheetSize.Compact,
        label = "Compact · 42 % de la pantalla",
        title = "Opciones del negocio",
        subtitle = "Gestiona tu espacio de trabajo",
        icon = ArxIcons.Settings
    ) {
        PreviewOption("Editar información", ArxIcons.Edit)
        PreviewOption("Gestionar miembros", ArxIcons.TeamOutline)
        PreviewOption("Compartir invitación", ArxIcons.Link)
    }
}

@ArxSheetPreviews
@Composable
private fun PreviewExpanded() {
    SheetPreviewFrame(
        size = ArxBottomSheetSize.Expanded,
        label = "Expanded · 78 % · formulario con footer fijo",
        title = "Crear negocio",
        subtitle = "Define los datos de tu espacio",
        icon = ArxIcons.Event,
        footer = { PreviewFooterRow(primary = "Crear negocio", secondary = "Cancelar") }
    ) {
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

        PreviewOption("Logotipo del negocio", ArxIcons.Photo)
        PreviewOption("Invitar miembros", ArxIcons.GroupAdd)
    }
}

@ArxSheetPreviews
@Composable
private fun PreviewFull() {
    SheetPreviewFrame(
        size = ArxBottomSheetSize.Full,
        label = "Full · 96 % · scroll + botón cerrar",
        title = "Configurar negocio",
        subtitle = "Personaliza tu espacio de trabajo",
        icon = ArxIcons.TeamOutline,
        showCloseButton = true,
        footer = { PreviewFooterRow(primary = "Guardar", secondary = "Descartar") }
    ) {
        Text(
            "Información general",
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(Modifier.height(ArxSpacing.sm))

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

        PreviewOption("Notificaciones del negocio", ArxIcons.Notification)
        PreviewOption("Permitir invitaciones", ArxIcons.Link)
        PreviewOption("Mostrar actividad reciente", ArxIcons.Timeline)
        PreviewOption("Sincronización automática", ArxIcons.Sync)
        PreviewOption("Permisos de operadores", ArxIcons.Shield)
    }
}

@ArxSheetPreviews
@Composable
private fun PreviewNoHeader() {
    val colors = MaterialTheme.colorScheme

    SheetPreviewFrame(
        size = ArxBottomSheetSize.WrapContent,
        label = "Sin encabezado · solo handle + contenido propio (perfil)"
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = ArxSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)
        ) {
            Box(
                modifier = Modifier
                    .size(ArxSpacing.giant)
                    .background(colors.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "A",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colors.onPrimaryContainer
                )
            }

            Column {
                Text(
                    text = "Alex",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Administrador",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )
            }
        }

        HorizontalDivider(color = colors.outlineVariant)

        PreviewOption("Notificaciones", ArxIcons.Notification)
        PreviewOption("Ajustes", ArxIcons.Settings)
        PreviewOption("Cerrar sesión", ArxIcons.Logout)
    }
}