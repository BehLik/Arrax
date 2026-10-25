package com.example.arrax.ui.screen.business

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.arrax.core.designsystem.components.navigation.ArxBottomBar
import com.example.arrax.core.designsystem.components.navigation.ArxBottomBarItem
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.navigation.BusinessCustomers
import com.example.arrax.core.navigation.BusinessEvents
import com.example.arrax.core.navigation.BusinessHome
import com.example.arrax.core.navigation.BusinessMore

private const val TAB_HOME = "business_home"
private const val TAB_EVENTS = "business_events"
private const val TAB_CUSTOMERS = "business_customers"
private const val TAB_MORE = "business_more"

private val businessTabs = listOf(
    ArxBottomBarItem("Resumen", ArxIcons.HomeOutline, ArxIcons.HomeFilled, TAB_HOME),
    ArxBottomBarItem("Eventos", ArxIcons.Event, ArxIcons.Event, TAB_EVENTS),
    ArxBottomBarItem("Clientes", ArxIcons.TeamOutline, ArxIcons.TeamFilled, TAB_CUSTOMERS),
    ArxBottomBarItem("Más", ArxIcons.More, ArxIcons.More, TAB_MORE)
)

/** Pestañas del negocio: si el destino actual no es una de ellas, la barra se oculta. */
@Composable
fun BusinessScaffold(
    nav: NavHostController,
    businessId: String,
    content: @Composable (Modifier) -> Unit
) {
    val destination = nav.currentBackStackEntryAsState().value?.destination

    val currentTab: String? = when {
        destination?.hasRoute<BusinessHome>() == true -> TAB_HOME
        destination?.hasRoute<BusinessEvents>() == true -> TAB_EVENTS
        destination?.hasRoute<BusinessCustomers>() == true -> TAB_CUSTOMERS
        destination?.hasRoute<BusinessMore>() == true -> TAB_MORE
        else -> null
    }

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        bottomBar = {
            if (currentTab != null) {
                ArxBottomBar(
                    items = businessTabs,
                    currentRoute = currentTab,
                    onItemClick = { tab ->
                        val target: Any = when (tab) {
                            TAB_HOME -> BusinessHome(businessId)
                            TAB_EVENTS -> BusinessEvents(businessId)
                            TAB_CUSTOMERS -> BusinessCustomers(businessId)
                            else -> BusinessMore(businessId)
                        }
                        if (tab != currentTab) {
                            nav.navigate(target) {
                                popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { padding ->
        content(Modifier.padding(padding))
    }
}