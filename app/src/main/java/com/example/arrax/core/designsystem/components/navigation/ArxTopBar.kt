package com.example.arrax.core.designsystem.components.navigation

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.feedback.ArxConnectionIndicator
import com.example.arrax.core.designsystem.components.feedback.ArxConnectionStatus
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

/** Variante visual de la barra superior. */
enum class ArxTopBarVariant {
    /** Barra compacta de las pantallas secundarias (comportamiento de siempre). */
    DEFAULT,

    /** Cabecera alta, exclusiva del Lobby principal: logo + nombre + subtítulo + conectividad. */
    MENU
}

object ArxTopBarDefaults {
    const val MenuSubtitle = "Asistente de Registro y Respuesta Automatizada eXtensible"
}

/**
 * Barra superior de Arrax.
 *
 * DEFAULT (por defecto): compacta, con navegación + título + acciones. Es lo que ya usabas.
 *
 * MENU (solo Lobby): cabecera alta con [logoRes], el nombre ([title], normalmente "Arrax"),
 * el [subtitle] y, si pasas [connectionStatus], el indicador de conectividad al final.
 * En esta variante se ignoran [navigationType], [centeredTitle], [titleIconRes] y [scrollBehavior]:
 * el Lobby es la raíz y la cabecera es fija. [actions] se muestra después del indicador.
 *
 * [connectionStatus] llega ya resuelto desde el ViewModel; la barra no consulta la red.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArxTopBar(
    title: String,
    modifier: Modifier = Modifier,
    centeredTitle: Boolean = true,
    navigationType: ArxTopBarNavigationType = ArxTopBarNavigationType.NONE,
    onNavigationClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    titleIconRes: Int? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    variant: ArxTopBarVariant = ArxTopBarVariant.DEFAULT,
    subtitle: String = ArxTopBarDefaults.MenuSubtitle,
    logoRes: Int? = null,
    connectionStatus: ArxConnectionStatus? = null
) {
    if (variant == ArxTopBarVariant.MENU) {
        ArxMenuHeader(
            title = title,
            subtitle = subtitle,
            logoRes = logoRes,
            connectionStatus = connectionStatus,
            actions = actions,
            modifier = modifier
        )
        return
    }

    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        scrolledContainerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.onBackground,
        navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
        actionIconContentColor = MaterialTheme.colorScheme.primary
    )

    val navigationIcon: @Composable () -> Unit = {
        when (navigationType) {
            ArxTopBarNavigationType.BACK -> {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        painter = painterResource(id = ArxIcons.Back),
                        contentDescription = "Volver"
                    )
                }
            }
            ArxTopBarNavigationType.MENU -> {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        painter = painterResource(id = ArxIcons.Menu),
                        contentDescription = "Menú principal"
                    )
                }
            }
            ArxTopBarNavigationType.NONE -> {}
        }
    }

    val titleContent: @Composable () -> Unit = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (titleIconRes != null) {
                Icon(
                    painter = painterResource(id = titleIconRes),
                    contentDescription = null, // decorativo: el título ya lo describe
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(ArxSpacing.xs))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    if (centeredTitle) {
        CenterAlignedTopAppBar(
            title = titleContent,
            modifier = modifier,
            navigationIcon = navigationIcon,
            actions = actions,
            colors = colors,
            scrollBehavior = scrollBehavior
        )
    } else {
        TopAppBar(
            title = titleContent,
            modifier = modifier,
            navigationIcon = navigationIcon,
            actions = actions,
            colors = colors,
            scrollBehavior = scrollBehavior
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
/** Cabecera alta del Lobby. Privada: se usa solo a través de ArxTopBar(variant = MENU). */
@Composable
private fun ArxMenuHeader(
    title: String,
    subtitle: String,
    logoRes: Int?,
    connectionStatus: ArxConnectionStatus?,
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .heightIn(min = 88.dp)
            .padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (logoRes != null) {
            Image(
                painter = painterResource(id = logoRes),
                contentDescription = null, // decorativo: "Arrax" ya está escrito al lado
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(ArxSpacing.sm))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.semantics { heading() }
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (connectionStatus != null) {
            Spacer(modifier = Modifier.width(ArxSpacing.xs))
            ArxConnectionIndicator(status = connectionStatus)
        }

        Row(verticalAlignment = Alignment.CenterVertically, content = actions)
    }
}

enum class ArxTopBarNavigationType {
    NONE, BACK, MENU
}

/** Acción de icono para el slot `actions` de [ArxTopBar] (ej. Buscar). Siempre lleva descripción. */
@Composable
fun ArxTopBarAction(
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription
        )
    }
}

// ───────────────────────── Previews (Light / Dark) ─────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArxTopBarPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column {
                // Lobby. logoRes es un marcador: pasa aquí tu logo real.
                ArxTopBar(
                    title = "Arrax",
                    variant = ArxTopBarVariant.MENU,
                    logoRes = ArxIcons.Lot,
                    connectionStatus = ArxConnectionStatus.Online,
                    actions = {
                        ArxTopBarAction(
                            iconRes = ArxIcons.NotificationOutline,
                            contentDescription = "Notificaciones",
                            onClick = {}
                        )
                    }
                )
                ArxTopBar(
                    title = "Arrax",
                    variant = ArxTopBarVariant.MENU,
                    logoRes = ArxIcons.Lot,
                    connectionStatus = ArxConnectionStatus.Offline
                )
                // Pantalla secundaria (sin cambios).
                ArxTopBar(
                    title = "Pedidos",
                    navigationType = ArxTopBarNavigationType.BACK,
                    titleIconRes = ArxIcons.Invoice,
                    centeredTitle = false,
                    actions = {
                        ArxTopBarAction(
                            iconRes = ArxIcons.Search,
                            contentDescription = "Buscar",
                            onClick = {}
                        )
                    }
                )
            }
        }
    }
}

@Preview(name = "TopBar – Light", showBackground = false)
@Composable
fun ArxTopBarPreviewLight() = ArxTopBarPreviewContent()

@Preview(name = "TopBar – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxTopBarPreviewDark() = ArxTopBarPreviewContent()