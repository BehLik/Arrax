package com.example.arrax.core.designsystem.components.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

/**
 * Barra superior minimalista: navegación + título + acciones opcionales. Sin sombras ni títulos gigantes.
 *
 * @param titleIconRes icono opcional a la izquierda del título (ej. el recibo de Pedidos).
 * @param scrollBehavior opcional, para que la barra reaccione al scroll.
 * Para las acciones usa [ArxTopBarAction] (cada una mide 48dp).
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
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
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
                ArxTopBar(
                    title = "Extracción de cortes",
                    navigationType = ArxTopBarNavigationType.MENU
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