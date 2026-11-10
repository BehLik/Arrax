package com.example.arrax.ui.screen.events.shell

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.navigation.ArxBottomBar
import com.example.arrax.core.designsystem.components.navigation.ArxBottomBarItem
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.navigation.EventDeliveries
import com.example.arrax.core.navigation.EventHome
import com.example.arrax.core.navigation.EventMore
import com.example.arrax.core.navigation.EventOrderDetail
import com.example.arrax.core.navigation.EventOrderEdit
import com.example.arrax.core.navigation.EventOrders
import com.example.arrax.core.navigation.EventShell
import com.example.arrax.core.navigation.EventWeighing
import com.example.arrax.core.navigation.WeighingMode
import com.example.arrax.ui.root.navigation.ArraxTransitions
import com.example.arrax.ui.root.navigation.safeNavigate
import com.example.arrax.ui.screen.events.deliveries.EventDeliveriesScreen
import com.example.arrax.ui.screen.events.home.EventHomeScreen
import com.example.arrax.ui.screen.events.more.EventMoreScreen
import com.example.arrax.ui.screen.events.orders.EventOrderScreen

private enum class EventTab(val key: String) {
    HOME("event_home"),
    ORDERS("event_orders"),
    DELIVERIES("event_deliveries"),
    MORE("event_more")
}

@Composable
fun EventShellScreen(args: EventShell, outerNav: NavController) {
    val innerNav = rememberNavController()
    val backStack by innerNav.currentBackStackEntryAsState()
    val dest = backStack?.destination

    val currentTab = when {
        dest?.hasRoute<EventOrders>() == true -> EventTab.ORDERS
        dest?.hasRoute<EventDeliveries>() == true -> EventTab.DELIVERIES
        dest?.hasRoute<EventMore>() == true -> EventTab.MORE
        else -> EventTab.HOME
    }

    val items = remember {
        listOf(
            ArxBottomBarItem(
                label = "Inicio",
                iconRes = ArxIcons.HomeOutline,
                selectedIconRes = ArxIcons.HomeFilled,
                route = EventTab.HOME.key
            ),
            ArxBottomBarItem(label = "Pedidos", iconRes = ArxIcons.Order, route = EventTab.ORDERS.key),
            ArxBottomBarItem(label = "Entregas", iconRes = ArxIcons.Delivery, route = EventTab.DELIVERIES.key),
            ArxBottomBarItem(label = "Más", iconRes = ArxIcons.More, route = EventTab.MORE.key)
        )
    }

    fun goTo(tab: EventTab) {
        if (tab == currentTab) return
        val route: Any = when (tab) {
            EventTab.HOME -> EventHome(args.businessId, args.eventId)
            EventTab.ORDERS -> EventOrders(args.businessId, args.eventId)
            EventTab.DELIVERIES -> EventDeliveries(args.businessId, args.eventId)
            EventTab.MORE -> EventMore(args.businessId, args.eventId)
        }
        innerNav.navigate(route) {
            popUpTo<EventHome> { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    ArxBackground(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = innerNav,
            startDestination = EventHome(args.businessId, args.eventId),
            enterTransition = ArraxTransitions.tabEnter,
            exitTransition = ArraxTransitions.tabExit,
            popEnterTransition = ArraxTransitions.tabEnter,
            popExitTransition = ArraxTransitions.tabExit
        ) {
            composable<EventHome> {
                EventHomeScreen(
                    onBack = { outerNav.popBackStack() },
                    onEditEventClick = {},
                    onEditEventNameClick = {},
                    onSeeDetailClick = {},
                    onMakeDeliveriesClick = { goTo(EventTab.DELIVERIES) },
                    onMakeOrdersClick = { outerNav.safeNavigate(EventOrderEdit(args.businessId, args.eventId)) },
                    onSeeAllOrdersClick = { goTo(EventTab.ORDERS) },
                    onOrderClick = { id ->
                        outerNav.safeNavigate(EventOrderDetail(args.businessId, args.eventId, id))
                    },
                    onHandsFreeWeighingClick = {
                        outerNav.safeNavigate(
                            EventWeighing(args.businessId, args.eventId, WeighingMode.BY_CUT, handsFree = true)
                        )
                    }
                )
            }
            composable<EventOrders> { EventOrderScreen() }
            composable<EventDeliveries> { EventDeliveriesScreen() }
            composable<EventMore> { EventMoreScreen() }
        }

        ArxBottomBar(
            items = items,
            currentRoute = currentTab.key,
            onItemClick = { key -> EventTab.entries.first { it.key == key }.let(::goTo) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}