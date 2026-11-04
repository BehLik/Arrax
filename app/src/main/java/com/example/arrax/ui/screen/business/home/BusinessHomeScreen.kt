package com.example.arrax.ui.screen.business.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
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
    onMoreClick: () -> Unit = {},
    onAlertsClick: () -> Unit = {},
    onCustomersClick: () -> Unit = {},
//    onTeamClick: () -> Unit = {},
//    onCutsClick: () -> Unit = {},
    onBackClik:()->Unit={},
    viewModel: BusinessHomeViewModel = hiltViewModel()
) {
    BackHandler(onBack = onBack)

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    BusinessHomeContent(
        state = state,
        onBusinessClick = onBusinessClick,
        onMoreClick = onMoreClick,
        onEventClick = onEventClick,
        onAlertsClick = onAlertsClick,
        onCreateEventClick = onCreateEventClick,
        onCustomersClick = onCustomersClick,
//        onTeamClick = onTeamClick,
//        onCutsClick = onCutsClick,
        onBackClik=onBackClik,
        modifier = modifier
    )
}

@Composable
internal fun BusinessHomeContent(
    state: BusinessHomeState,
    onBusinessClick: () -> Unit,
    onMoreClick: () -> Unit,
    onEventClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onCreateEventClick: () -> Unit,
    onCustomersClick: () -> Unit,
//    onTeamClick: () -> Unit,
//    onCutsClick: () -> Unit,
    onBackClik:()-> Unit,
    modifier: Modifier = Modifier
) {
    ArxBackground(
        modifier = modifier.fillMaxSize(),
        decoration = ArxDecoration.None
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
            topBar = {
                HomeHeader(
                    onMoreClick = onMoreClick,
                    onBackClik = onBackClik
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = ArxSpacing.sm,
                        vertical = ArxSpacing.md
                    )
                    .padding(bottom = ArxSpacing.xl),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
            ) {
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
                    NextEventBanner(
                        event = next,
                        onClick = onEventClick
                    )
                }

                HomeAlertsSection(
                    alerts = state.alerts,
                    onAlertsClick = onAlertsClick
                )

                SalesSummaryCard(
                    total = state.salesTotal,
                    slices = state.salesBreakdown
                )

                QuickActionsSection(
                    onCreateEventClick = onCreateEventClick,
                    onCustomersClick = onCustomersClick
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 900,
    name = "Business Home - Light"
)
@Composable
private fun BusinessHomePreviewLight() {
    ArxTheme(darkTheme = false) {
        BusinessHomePreviewContent()
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 900,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    name = "Business Home - Dark"
)
@Composable
private fun BusinessHomePreviewDark() {
    ArxTheme(darkTheme = true) {
        BusinessHomePreviewContent()
    }
}

@Composable
private fun BusinessHomePreviewContent() {
    BusinessHomeContent(
        state = BusinessHomeState(
            businessName = " San José",
            roleLabel = "Owner",
            memberCount = 8,
            upcomingEventsCount = 2,
            customersCount = 24,
            nextEvent = HomeNextEventUi(
                whenLabel = "Hoy • 6:00 PM",
                message = "Revisa la estimación de cortes y la disponibilidad de insumos para el evento de mañana."
            ),
            alerts = listOf(
//                HomeAlertUi(
//                    "1",
//                    "Evento del sábado",
//                    "Faltan 2 días • 6 cerdos estimados",
//                    "En preparación",
//                    ArxTone.Success,
//                    ArxIcons.Event
//                ),
//                HomeAlertUi(
//                    "2",
//                    "Invitación por vencer",
//                    "Código #AB12 • Vence en 3 días",
//                    "Atención",
//                    ArxTone.Warning,
//                    ArxIcons.GroupAdd
//                ),
                HomeAlertUi(
                    "3",
                    "Pedidos pendientes de pesar",
                    "Evento del sábado • 4 pedidos",
                    "Pendiente",
                    ArxTone.Info,
                    ArxIcons.Order
                )
            ),
            salesTotal = "$ 3,820.00",
            salesBreakdown = listOf(
                HomeSalesSliceUi(
                    "Cortes",
                    "68%",
                    0.68f,
                    ArxTone.Primary
                ),
                HomeSalesSliceUi(
                    "Clientes",
                    "22%",
                    0.22f,
                    ArxTone.Info
                ),
                HomeSalesSliceUi(
                    "Otros",
                    "10%",
                    0.10f,
                    ArxTone.Neutral
                )
            )
        ),
        onBusinessClick = {},
        onMoreClick = {},
        onEventClick = {},
        onAlertsClick = {},
        onCreateEventClick = {},
        onCustomersClick = {},
//        onTeamClick = {},
//        onCutsClick = {},
        onBackClik = {}
    )
}
