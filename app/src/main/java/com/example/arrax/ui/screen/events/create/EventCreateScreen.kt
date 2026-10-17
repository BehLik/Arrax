package com.example.arrax.ui.screen.events.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.R
import com.example.arrax.core.designsystem.components.display.ArxEmptyState
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.ui.screen.events.create.component.EventBasicDataStep
import com.example.arrax.ui.screen.events.create.component.EventCutsPriceStep
import com.example.arrax.ui.screen.events.create.component.EventCutsSelectionStep

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCreateScreen(
    onBack: () -> Unit,
    onEventCreated: (eventId: String) -> Unit,
    viewModel: EventCreateViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.createdEventId) {
        state.createdEventId?.let { onEventCreated(it) }
    }

    Scaffold(
        topBar = {
            ArxTopBar(
                title = stringResource(
                    when (state.step) {
                        0 -> R.string.event_create_title_basic
                        1 -> R.string.event_create_title_cuts
                        else -> R.string.event_create_title_prices
                    }
                ),
                navigationType = ArxTopBarNavigationType.BACK,
                onNavigationClick = { if (state.step == 0) onBack() else viewModel.onBack() }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isCheckingRole -> ArxLoading()
                !state.isAdmin -> ArxEmptyState(
                    title = stringResource(R.string.event_create_no_permission_title),
                    description = stringResource(R.string.event_create_no_permission_description),
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
                    pricesByCutId = state.pricesByCutId, // now Map<String, PriceInput>
                    canConfirm = state.canConfirm,
                    onPriceChange = viewModel::onPriceChange,
                    onConfirm = viewModel::onConfirm
                )
            }
        }
    }
}
