package com.example.arrax.ui.screen.events.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.core.designsystem.components.display.ArxEmptyState
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.ui.screen.events.create.component.EventBasicDataStep
import com.example.arrax.ui.screen.events.create.component.EventCutsPriceStep
import com.example.arrax.ui.screen.events.create.component.EventCutsSelectionStep

@Composable
fun EventCreateScreen(
    onBack: () -> Unit,
    onEventCreated: (eventId: String) -> Unit,
    viewModel: EventCreateViewmodel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.createdEventId) {
        state.createdEventId?.let { onEventCreated(it) }
    }

    Scaffold(
        topBar = {
            // PENDIENTE: mismo problema que EventDetailScreen - falta el nombre
            // real del callback de "atras" de ArxTopBar.
            ArxTopBar(
                title = when (state.step) {
                    0 -> "Nuevo evento - Datos"
                    1 -> "Nuevo evento - Cortes"
                    else -> "Nuevo evento - Precios"
                },
                navigationType = ArxTopBarNavigationType.BACK,
                onNavigationClick = { if (state.step == 0) onBack() else viewModel.onBack() }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isCheckingRole -> ArxLoading()
                !state.isAdmin -> ArxEmptyState(
                    title = "Sin permiso",
                    description = "Solo un administrador puede crear eventos.",
                    iconRes = ArxIcons.Lock
                )
                state.isSubmitting -> ArxLoading()
                state.step == 0 -> EventBasicDataStep(
                    name = state.name,
                    dateMillis = state.dateMillis,
                    pigCount = state.pigCount,
                    canContinue = state.canContinueFromBasicData,
                    onNameChange = viewModel::onNameChange,
                    onDateChange = viewModel::onDateChange,
                    onPigCountChange = viewModel::onPigCountChange,
                    onContinue = viewModel::onContinueFromBasicData
                )
                state.step == 1 -> EventCutsSelectionStep(
                    cuts = state.availableCuts,
                    selectedCutIds = state.selectedCutIds,
                    isLoading = state.isLoadingCatalog,
                    canContinue = state.canContinueFromCuts,
                    onToggleCut = viewModel::onToggleCut,
                    onContinue = viewModel::onContinueFromCuts
                )
                else -> EventCutsPriceStep(
                    selectedCuts = state.selectedCuts,
                    pricesByCutId = state.pricesByCutId,
                    canConfirm = state.canConfirm,
                    onPriceChange = viewModel::onPriceChange,
                    onConfirm = viewModel::onConfirm
                )
            }
        }
    }
}
