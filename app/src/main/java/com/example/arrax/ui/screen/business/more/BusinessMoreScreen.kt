package com.example.arrax.ui.screen.business.more

import androidx.annotation.DrawableRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.ui.screen.business.BusinessListRow
import com.example.arrax.ui.screen.business.BusinessScreenSurface
import com.example.arrax.ui.screen.business.BusinessTone
import com.example.arrax.ui.screen.business.BusinessToneBadge
import com.example.arrax.ui.screen.business.BusinessTopBar

private data class MoreOption(
    @DrawableRes val icon: Int,
    val title: String,
    val subtitle: String,
    val onClick: () -> Unit
)

@Composable
fun BusinessMoreScreen(
    modifier: Modifier = Modifier,
    onTeamClick: () -> Unit = {},
    onCatalogClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    viewModel: BusinessMoreViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BusinessMoreContent(
        state = state,
        onTeamClick = onTeamClick,
        onCatalogClick = onCatalogClick,
        onHistoryClick = onHistoryClick,
        onSettingsClick = onSettingsClick,
        onHelpClick = onHelpClick,
        onAboutClick = onAboutClick,
        onLogoutClick = onLogoutClick,
        modifier = modifier
    )
}

@Composable
internal fun BusinessMoreContent(
    state: BusinessMoreState,
    onTeamClick: () -> Unit,
    onCatalogClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onAboutClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val admin = listOf(
        MoreOption(ArxIcons.TeamOutline, "Equipo y permisos", "Miembros, roles, invitaciones, QR y códigos", onTeamClick),
        MoreOption(ArxIcons.Box, "Catálogo de productos y cortes", "Productos disponibles y configuración", onCatalogClick),
        MoreOption(ArxIcons.Timeline, "Historial del negocio", "Eventos cerrados y resultados históricos", onHistoryClick),
        MoreOption(ArxIcons.Settings, "Ajustes del negocio", "Datos del negocio y preferencias", onSettingsClick)
    )
    val info = listOf(
        MoreOption(ArxIcons.Info, "Ayuda y soporte", "Centro de ayuda y contacto", onHelpClick),
        MoreOption(ArxIcons.System, "Acerca de Arrax", "Versión ${state.appVersion}", onAboutClick)
    )

    BusinessScreenSurface(modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            BusinessTopBar(title = "Más")

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = ArxSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.lg)
            ) {
                MoreSection(title = "Administración del negocio", options = admin)
                MoreSection(title = "Información", options = info)
            }

            OutlinedButton(
                onClick = onLogoutClick,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.sm)
                    .heightIn(min = 48.dp)
            ) {
                ArxIcon(icon = ArxIcons.Logout, tint = MaterialTheme.colorScheme.error, size = 20.dp)
                Text(
                    text = "Cerrar sesión",
                    modifier = Modifier.padding(start = ArxSpacing.xs),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun MoreSection(title: String, options: List<MoreOption>) {
    Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        options.forEach { option ->
            BusinessListRow(
                title = option.title,
                subtitle = option.subtitle,
                leading = { BusinessToneBadge(icon = option.icon, tone = BusinessTone.Primary) },
                onClick = option.onClick
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Preview(showBackground = true, widthDp = 360, heightDp = 780, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BusinessMorePreview() {
    ArxTheme(darkTheme = true) {
        BusinessMoreContent(
            state = BusinessMoreState(appVersion = "1.0.0"),
            onTeamClick = {}, onCatalogClick = {}, onHistoryClick = {}, onSettingsClick = {},
            onHelpClick = {}, onAboutClick = {}, onLogoutClick = {}
        )
    }
}