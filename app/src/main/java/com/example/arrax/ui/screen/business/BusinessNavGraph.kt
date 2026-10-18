package com.example.arrax.ui.screen.business

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.arrax.core.navigation.BusinessHome
import com.example.arrax.core.navigation.EventCreate
import com.example.arrax.core.navigation.EventHome
import com.example.arrax.ui.screen.business.home.BusinessHomeScreen
import com.example.arrax.ui.screen.events.create.EventCreateScreen
import com.example.arrax.ui.screen.events.detail.EventDetailScreen

fun NavGraphBuilder.businessGraph(nav: NavController) {
//    composable<BusinessHome> { entry ->
//        val args = entry.toRoute<BusinessHome>()
//        BusinessHomeScreen(
//            onCreateEventClick = { nav.navigate(EventCreate(args.businessId)) },
//            onEventClick = { eventId -> nav.navigate(EventDetail(args.businessId, eventId)) },
//            onBack = nav::popBackStack
//        )
//    }
    composable<EventCreate> { entry ->
        val args = entry.toRoute<EventCreate>()
        EventCreateScreen(
            onBack = nav::popBackStack,
            onEventCreated = { eventId ->
                nav.navigate(EventHome(args.businessId, eventId)) {
                    popUpTo<EventCreate> { inclusive = true }
                }
            }
        )
    }
    composable<EventHome> { EventDetailScreen(onBack = nav::popBackStack) }
}
