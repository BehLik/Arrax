package com.example.arrax.ui.screen.business.alerts

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.display.ArxTone
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarVariant
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.ui.screen.business.alerts.component.AlertsEmptyState
import com.example.arrax.ui.screen.business.alerts.component.AlertsList
import com.example.arrax.ui.screen.business.alerts.component.AlertsTabs
import com.example.arrax.ui.screen.business.alerts.component.MarkAllReadButton

@Composable
fun BusinessAlertsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onAlertClick: (alertId: String) -> Unit = {},
    viewModel: BusinessAlertsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BusinessAlertsContent(
        state = state,
        onBack = onBack,
        onTabSelected = viewModel::onTabSelected,
        onAlertClick = onAlertClick,
        onMarkAllRead = viewModel::onMarkAllRead,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BusinessAlertsContent(
    state: BusinessAlertsState,
    onBack: () -> Unit,
    onTabSelected: (AlertsTab) -> Unit,
    onAlertClick: (String) -> Unit,
    onMarkAllRead: () -> Unit,
    modifier: Modifier = Modifier
) {
    val visible = state.alerts.filter {
        state.selectedTab == AlertsTab.All || it.category == state.selectedTab
    }

    ArxScreenSurface(modifier) {
        ArxBackground(decoration = ArxDecoration.None) {
            Column(modifier = Modifier.fillMaxSize()) {
                ArxTopBar(
                    title = "Alertas y recordatorios",
                    centeredTitle = false,
                    navigationType = ArxTopBarNavigationType.BACK,
                    onNavigationClick = onBack
                )

                AlertsTabs(selected = state.selectedTab, onSelected = onTabSelected)

                if (visible.isEmpty()) {
                    AlertsEmptyState(modifier = Modifier.weight(1f))
                } else {
                    AlertsList(
                        alerts = visible,
                        onAlertClick = onAlertClick,
                        modifier = Modifier.weight(1f)
                    )
                }

                MarkAllReadButton(
                    enabled = state.alerts.any { it.isUnread },
                    onClick = onMarkAllRead
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Preview(
    showBackground = true, widthDp = 360, heightDp = 780,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun BusinessAlertsPreview() {
    ArxTheme(darkTheme = isSystemInDarkTheme()) {
        BusinessAlertsContent(
            state = BusinessAlertsState(
                alerts = listOf(
                    AlertUi("1", AlertsTab.Events, "Evento próximo", "Mañana • 6:00 PM", "Revisa la estimación de cortes y la disponibilidad.", ArxIcons.Event, ArxTone.Warning, true),
                    AlertUi("2", AlertsTab.Invitations, "Invitación por vencer", "Código #AB12 • Vence en 3 días", "Comparte o genera una nueva invitación.", ArxIcons.GroupAdd, ArxTone.Warning, true),
                    AlertUi("3", AlertsTab.Orders, "Pedidos pendientes de pesar", "Evento del sábado • 4 pedidos", "Requieren pesaje para continuar.", ArxIcons.Order, ArxTone.Info),
                    AlertUi("4", AlertsTab.All, "Nuevo miembro", "Carlos Ramírez • Rol: Operador", "Se unió al negocio.", ArxIcons.User, ArxTone.Success),
                    AlertUi("5", AlertsTab.Events, "Evento cerrado", "Evento #10 • 12 oct", "Revisa el resumen y el corte de caja.", ArxIcons.Check, ArxTone.Neutral)
                )
            ),
            onBack = {}, onTabSelected = {}, onAlertClick = {}, onMarkAllRead = {}
        )
    }
}