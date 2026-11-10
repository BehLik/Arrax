package com.example.arrax.ui.screen.events.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.display.ArxSectionHeader
import com.example.arrax.core.designsystem.components.navigation.ArxBottomBar
import com.example.arrax.core.designsystem.components.navigation.ArxBottomBarItem
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.ui.screen.events.home.component.EventEditPill
import com.example.arrax.ui.screen.events.home.component.EventHeroCard
import com.example.arrax.ui.screen.events.home.component.EventQuickActionsRow
import com.example.arrax.ui.screen.events.home.component.EventSummaryCard
import com.example.arrax.ui.screen.events.home.component.RecentOrderRowCard

@Composable
fun EventHomeScreen(
    onBack: () -> Unit={},
    onEditEventClick: () -> Unit,
    onEditEventNameClick: () -> Unit,
    onMakeOrdersClick: () -> Unit,
    onHandsFreeWeighingClick: () -> Unit,
    onMakeDeliveriesClick: () -> Unit,
    onSeeDetailClick: () -> Unit,
    onSeeAllOrdersClick: () -> Unit,
    onOrderClick: (String) -> Unit,
    onBottomBarNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventHomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val handleEvent by rememberUpdatedState<(EventHomeEvent) -> Unit> { event ->
        when (event) {
            EventHomeEvent.NavigateBack -> onBack()
            EventHomeEvent.EditEvent -> onEditEventClick()
            EventHomeEvent.EditEventName -> onEditEventNameClick()
            EventHomeEvent.MakeOrders -> onMakeOrdersClick()
            EventHomeEvent.HandsFreeWeighing -> onHandsFreeWeighingClick()
            EventHomeEvent.MakeDeliveries -> onMakeDeliveriesClick()
            EventHomeEvent.SeeDetail -> onSeeDetailClick()
            EventHomeEvent.SeeAllOrders -> onSeeAllOrdersClick()
            is EventHomeEvent.OpenOrder -> onOrderClick(event.orderId)
            is EventHomeEvent.NavigateToRoute -> onBottomBarNavigate(event.route)
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { handleEvent(it) }
    }

    EventHomeContent(
        state = state,
        onBackClick = viewModel::onBackClick,
        onEditEventClick = viewModel::onEditEventClick,
        onEditEventNameClick = viewModel::onEditEventNameClick,
        onMakeOrdersClick = viewModel::onMakeOrdersClick,
        onHandsFreeWeighingClick = viewModel::onHandsFreeWeighingClick,
        onMakeDeliveriesClick = viewModel::onMakeDeliveriesClick,
        onSeeDetailClick = viewModel::onSeeDetailClick,
        onSeeAllOrdersClick = viewModel::onSeeAllOrdersClick,
        onOrderClick = viewModel::onOrderClick,
        onBottomBarClick = viewModel::onBottomBarClick,
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventHomeContent(
    state: EventHomeState,
    onBackClick: () -> Unit,
    onEditEventClick: () -> Unit,
    onEditEventNameClick: () -> Unit,
    onMakeOrdersClick: () -> Unit,
    onHandsFreeWeighingClick: () -> Unit,
    onMakeDeliveriesClick: () -> Unit,
    onSeeDetailClick: () -> Unit,
    onSeeAllOrdersClick: () -> Unit,
    onOrderClick: (String) -> Unit,
    onBottomBarClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val bottomItems = remember {
        listOf(
            ArxBottomBarItem(
                label = "Inicio",
                iconRes = ArxIcons.HomeOutline,
                selectedIconRes = ArxIcons.HomeFilled,
                route = EventHomeState.ROUTE_HOME
            ),
            ArxBottomBarItem(
                label = "Pedidos",
                iconRes = ArxIcons.Order,
                route = EventHomeState.ROUTE_ORDERS
            ),
            ArxBottomBarItem(
                label = "Entregas",
                iconRes = ArxIcons.Delivery,
                route = EventHomeState.ROUTE_DELIVERIES
            ),
            ArxBottomBarItem(
                label = "Más",
                iconRes = ArxIcons.More,
                route = EventHomeState.ROUTE_MORE
            )
        )
    }

    ArxBackground(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            ArxTopBar(
                title = "Evento",
                centeredTitle = false,
                navigationType = ArxTopBarNavigationType.BACK,
                onNavigationClick = onBackClick,
                actions = {
                    EventEditPill(
                        onClick = onEditEventClick,
                        modifier = Modifier.padding(end = ArxSpacing.sm)
                    )
                }
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = ArxSpacing.md,
                    end = ArxSpacing.md,
                    top = ArxSpacing.sm,
                    bottom = 104.dp
                ),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
            ) {
                item(key = "hero") {
                    EventHeroCard(state = state, onEditNameClick = onEditEventNameClick)
                }

                item(key = "quick_actions") {
                    EventQuickActionsRow(
                        onMakeOrdersClick = onMakeOrdersClick,
                        onHandsFreeWeighingClick = onHandsFreeWeighingClick,
                        onMakeDeliveriesClick = onMakeDeliveriesClick
                    )
                }

                item(key = "summary") {
                    EventSummaryCard(state = state, onSeeDetailClick = onSeeDetailClick)
                }

                item(key = "recent_header") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ArxIcon(
                            icon = ArxIcons.List,
                            tint = MaterialTheme.colorScheme.primary,
                            size = 20.dp
                        )
                        Spacer(Modifier.width(ArxSpacing.xs))
                        ArxSectionHeader(
                            title = "Pedidos recientes",
                            actionLabel = "Ver todos",
                            onAction = onSeeAllOrdersClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                items(state.recentOrders, key = { it.id }) { order ->
                    RecentOrderRowCard(order = order, onClick = { onOrderClick(order.id) })
                }
            }
        }

        ArxBottomBar(
            items = bottomItems,
            currentRoute = state.currentRoute,
            onItemClick = onBottomBarClick,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}



@Composable
private fun EventHomePreviewContent() {
    EventHomeContent(
        state = EventHomeState(),
        onBackClick = {},
        onEditEventClick = {},
        onEditEventNameClick = {},
        onMakeOrdersClick = {},
        onHandsFreeWeighingClick = {},
        onMakeDeliveriesClick = {},
        onSeeDetailClick = {},
        onSeeAllOrdersClick = {},
        onOrderClick = {},
        onBottomBarClick = {}
    )
}

@Preview(name = "EventHome Light", showBackground = true, widthDp = 390, heightDp = 900)
@Composable
private fun EventHomeScreenLightPreview() {
    ArxTheme(darkTheme = false) { EventHomePreviewContent() }
}

@Preview(
    name = "EventHome Dark",
    showBackground = true,
    widthDp = 390,
    heightDp = 900,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun EventHomeScreenDarkPreview() {
    ArxTheme(darkTheme = true) { EventHomePreviewContent() }
}