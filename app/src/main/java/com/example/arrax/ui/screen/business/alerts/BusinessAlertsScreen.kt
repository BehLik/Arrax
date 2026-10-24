package com.example.arrax.ui.screen.business.alerts

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.ui.screen.business.BusinessDot
import com.example.arrax.ui.screen.business.BusinessListRow
import com.example.arrax.ui.screen.business.BusinessScreenSurface
import com.example.arrax.ui.screen.business.BusinessTabs
import com.example.arrax.ui.screen.business.BusinessTone
import com.example.arrax.ui.screen.business.BusinessToneBadge
import com.example.arrax.ui.screen.business.BusinessTopBar


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

    BusinessScreenSurface(modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            BusinessTopBar(title = "Alertas y recordatorios", onBack = onBack)

            BusinessTabs(
                labels = AlertsTab.entries.map { it.label() },
                selectedIndex = AlertsTab.entries.indexOf(state.selectedTab),
                onSelect = { onTabSelected(AlertsTab.entries[it]) },
                modifier = Modifier.padding(horizontal = ArxSpacing.md)
            )

            if (visible.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        ArxIcon(
                            icon = ArxIcons.Notification,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            size = 40.dp
                        )
                        Text(
                            text = "No hay alertas por ahora",
                            modifier = Modifier.padding(top = ArxSpacing.sm),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(ArxSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
                ) {
                    items(visible, key = { it.id }) { alert ->
                        BusinessListRow(
                            title = alert.title,
                            subtitle = alert.detail,
                            detail = alert.supporting,
                            tone = alert.tone.takeIf { alert.isUnread },
                            leading = { BusinessToneBadge(icon = alert.icon, tone = alert.tone) },
                            trailing = {
                                if (alert.isUnread) BusinessDot(tone = alert.tone)
                            },
                            onClick = { onAlertClick(alert.id) }
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = onMarkAllRead,
                enabled = state.alerts.any { it.isUnread },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.sm)
                    .heightIn(min = 48.dp)
            ) {
                ArxIcon(icon = ArxIcons.DoneAll, size = 20.dp)
                Text(
                    text = "Marcar todas como leídas",
                    modifier = Modifier.padding(start = ArxSpacing.xs),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

private fun AlertsTab.label() = when (this) {
    AlertsTab.All -> "Todas"
    AlertsTab.Events -> "Eventos"
    AlertsTab.Invitations -> "Invitaciones"
    AlertsTab.Orders -> "Pedidos"
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Preview(showBackground = true, widthDp = 360, heightDp = 780, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BusinessAlertsPreview() {
    ArxTheme(darkTheme = true) {
        BusinessAlertsContent(
            state = BusinessAlertsState(
                alerts = listOf(
                    AlertUi("1", AlertsTab.Events, "Evento próximo", "Mañana • 6:00 PM", "Revisa la estimación de cortes y la disponibilidad.", ArxIcons.Event, BusinessTone.Warning, true),
                    AlertUi("2", AlertsTab.Invitations, "Invitación por vencer", "Código #AB12 • Vence en 3 días", "Comparte o genera una nueva invitación.", ArxIcons.GroupAdd, BusinessTone.Warning, true),
                    AlertUi("3", AlertsTab.Orders, "Pedidos pendientes de pesar", "Evento del sábado • 4 pedidos", "Requieren pesaje para continuar.", ArxIcons.Order, BusinessTone.Info),
                    AlertUi("4", AlertsTab.All, "Nuevo miembro", "Carlos Ramírez • Rol: Operador", "Se unió al negocio.", ArxIcons.User, BusinessTone.Success),
                    AlertUi("5", AlertsTab.Events, "Evento cerrado", "Evento #10 • 12 oct", "Revisa el resumen y el corte de caja.", ArxIcons.Check, BusinessTone.Neutral)
                )
            ),
            onBack = {}, onTabSelected = {}, onAlertClick = {}, onMarkAllRead = {}
        )
    }
}