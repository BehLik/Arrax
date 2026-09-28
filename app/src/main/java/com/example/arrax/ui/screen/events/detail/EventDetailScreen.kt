package com.example.arrax.ui.screen.events.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.core.designsystem.components.display.ArxEmptyState
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.designsystem.components.feedback.ArxAlertDialog
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.domain.model.EventStatus
import com.example.arrax.ui.screen.events.detail.component.EventAdminActions
import com.example.arrax.ui.screen.events.detail.component.EventSummaryCard

@Composable
fun EventDetailScreen(
    onBack: () -> Unit,
    viewModel: EventDetailViewmodel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            // PENDIENTE: onBackClick no existe en tu ArxTopBar real. Necesito el
            // nombre real del callback de "atras" (o si se maneja distinto, p. ej.
            // via navigationType + un solo callback) para dejar esto compilable.
            ArxTopBar(
                title = state.eventWithCuts?.event?.name ?: "Evento",
                navigationType = ArxTopBarNavigationType.BACK,
                onNavigationClick = onBack
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> ArxLoading()
                state.eventWithCuts == null -> ArxEmptyState(
                    title = "Evento no encontrado",
                    description = "Puede que haya sido eliminado o que no tengas acceso.",
                    iconRes = ArxIcons.SearchOff
                )
                else -> {
                    val eventWithCuts = state.eventWithCuts!!
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        EventSummaryCard(eventWithCuts = eventWithCuts)

                        if (state.isAdmin && eventWithCuts.event.status == EventStatus.OPEN) {
                            Spacer(modifier = Modifier.height(16.dp))
                            EventAdminActions(
                                isClosing = state.isClosing,
                                onCloseEventClick = viewModel::onCloseEventClick
                            )
                        }
                    }
                }
            }

            if (state.showCloseConfirmation) {
                ArxAlertDialog(
                    title = "Cerrar evento",
                    message = "Esta accion no se puede deshacer desde la app. ¿Seguro que quieres cerrar el evento?",
                    confirmText = "Cerrar evento",
                    onConfirm = viewModel::onConfirmCloseEvent,
                    onDismiss = viewModel::onDismissCloseConfirmation
                )
            }
        }
    }
}
