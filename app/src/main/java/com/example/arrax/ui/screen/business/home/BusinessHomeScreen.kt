package com.example.arrax.ui.screen.business.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.backgrounds.ArxScreenSurface
import com.example.arrax.core.designsystem.components.display.ArxTone
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.ui.screen.business.home.component.ActiveBusinessCard
import com.example.arrax.ui.screen.business.home.component.HomeAlertsSection
import com.example.arrax.ui.screen.business.home.component.HomeHeader
import com.example.arrax.ui.screen.business.home.component.HomeStatsRow
import com.example.arrax.ui.screen.business.home.component.NextEventBanner
import com.example.arrax.ui.screen.business.home.component.QuickActionsSection
import com.example.arrax.ui.screen.business.home.component.SalesSummaryCard

@Composable
fun BusinessHomeScreen(
    onBack: () -> Unit,
    onCreateEventClick: () -> Unit,
    onEventClick: () -> Unit,
    modifier: Modifier = Modifier,
    onBusinessClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onAlertsClick: () -> Unit = {},
    onCustomersClick: () -> Unit = {},
    onTeamClick: () -> Unit = {},
    onCutsClick: () -> Unit = {},
    viewModel: BusinessHomeViewModel = hiltViewModel()
) {
    BackHandler(onBack = onBack)
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    BusinessHomeContent(
        state = state,
        onBusinessClick = onBusinessClick,
        onNotificationsClick = onNotificationsClick,
        onEventClick = onEventClick,
        onAlertsClick = onAlertsClick,
        onCreateEventClick = onCreateEventClick,
        onCustomersClick = onCustomersClick,
        onTeamClick = onTeamClick,
        onCutsClick = onCutsClick,
        modifier = modifier
    )
}

@Composable
internal fun BusinessHomeContent(
    state: BusinessHomeState,
    onBusinessClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onEventClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onCreateEventClick: () -> Unit,
    onCustomersClick: () -> Unit,
    onTeamClick: () -> Unit,
    onCutsClick: () -> Unit,
    modifier: Modifier = Modifier
) {

        ArxBackground(decoration = ArxDecoration.None) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = ArxSpacing.md)
                    .padding(bottom = ArxSpacing.xl),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
            ) {
                HomeHeader(
                    unread = state.unreadNotifications,
                    onNotificationsClick = onNotificationsClick
                )
                ActiveBusinessCard(
                    businessName = state.businessName,
                    roleLabel = state.roleLabel,
                    memberCount = state.memberCount,
                    onClick = onBusinessClick
                )
                HomeStatsRow(
                    upcomingEvents = state.upcomingEventsCount,
                    customers = state.customersCount
                )
                state.nextEvent?.let { next ->
                    NextEventBanner(event = next, onClick = onEventClick)
                }
                HomeAlertsSection(alerts = state.alerts, onAlertsClick = onAlertsClick)
                SalesSummaryCard(total = state.salesTotal, slices = state.salesBreakdown)
                QuickActionsSection(
                    onCreateEventClick = onCreateEventClick,
                    onCustomersClick = onCustomersClick,
                    onTeamClick = onTeamClick,
                    onCutsClick = onCutsClick
                )
            }
        }

}

@Preview(showBackground = true, widthDp = 360, heightDp = 900)
@Preview(
    showBackground = true, widthDp = 360, heightDp = 900,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun BusinessHomePreview() {
    ArxTheme(darkTheme = isSystemInDarkTheme()) {
        BusinessHomeContent(
            state = BusinessHomeState(
                businessName = "Granja San José",
                roleLabel = "Owner",
                memberCount = 8,
                unreadNotifications = 3,
                upcomingEventsCount = 2,
                customersCount = 24,
                nextEvent = HomeNextEventUi(
                    whenLabel = "Hoy • 6:00 PM",
                    message = "Revisa la estimación de cortes y la disponibilidad de insumos para el evento de mañana."
                ),
                alerts = listOf(
                    HomeAlertUi("1", "Evento del sábado", "Faltan 2 días • 6 cerdos estimados", "En preparación", ArxTone.Success, ArxIcons.Event),
                    HomeAlertUi("2", "Invitación por vencer", "Código #AB12 • Vence en 3 días", "Atención", ArxTone.Warning, ArxIcons.GroupAdd),
                    HomeAlertUi("3", "Pedidos pendientes de pesar", "Evento del sábado • 4 pedidos", "Pendiente", ArxTone.Info, ArxIcons.Order)
                ),
                salesTotal = "$ 3,820.00",
                salesBreakdown = listOf(
                    HomeSalesSliceUi("Cortes", "68%", 0.68f, ArxTone.Primary),
                    HomeSalesSliceUi("Clientes", "22%", 0.22f, ArxTone.Info),
                    HomeSalesSliceUi("Otros", "10%", 0.10f, ArxTone.Neutral)
                )
            ),
            onBusinessClick = {}, onNotificationsClick = {}, onEventClick = {}, onAlertsClick = {},
            onCreateEventClick = {}, onCustomersClick = {}, onTeamClick = {}, onCutsClick = {}
        )
    }
}