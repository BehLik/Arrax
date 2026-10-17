package com.example.arrax.ui.root.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.arrax.core.navigation.BusinessHome
import com.example.arrax.core.navigation.EventCreate
import com.example.arrax.core.navigation.EventDetail
import com.example.arrax.ui.screen.business.BusinessHomeScreen
import com.example.arrax.ui.screen.events.create.EventCreateScreen
import com.example.arrax.ui.screen.events.detail.EventDetailScreen

fun NavGraphBuilder.businessGraph(nav: NavController) {
    composable<BusinessHome> { entry ->
        val args = entry.toRoute<BusinessHome>()
        BusinessHomeScreen(
            onCreateEventClick = { nav.navigate(EventCreate(args.businessId)) },
            onEventClick = { eventId -> nav.navigate(EventDetail(args.businessId, eventId)) },
            onBack = nav::popBackStack
        )
    }
    composable<EventCreate> { entry ->
        val args = entry.toRoute<EventCreate>()
        EventCreateScreen(
            onBack = nav::popBackStack,
            onEventCreated = { eventId ->
                nav.navigate(EventDetail(args.businessId, eventId)) {
                    popUpTo<EventCreate> { inclusive = true }
                }
            }
        )
    }
    composable<EventDetail> { EventDetailScreen(onBack = nav::popBackStack) }
}
