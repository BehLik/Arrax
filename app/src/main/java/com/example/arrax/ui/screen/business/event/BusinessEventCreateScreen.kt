package com.example.arrax.ui.screen.business.event

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.components.display.ArxEmptyState
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.event.component.CutsStep
import com.example.arrax.ui.screen.business.event.component.DataStep
import com.example.arrax.ui.screen.business.event.component.DoneStep
import com.example.arrax.ui.screen.business.event.component.PricesStep
import com.example.arrax.ui.screen.business.event.component.StepIndicator
import com.example.arrax.ui.screen.business.event.component.SummaryStep
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessEventCreateScreen(
    onBack: () -> Unit,
    onEventCreated: (eventId: String) -> Unit,
    viewModel: BusinessEventCreateViewmodel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackbar.showSnackbar(it)
            viewModel.onErrorShown()
        }
    }

    val step = state.step
    val canLeaveDirectly = step == EventCreateStep.DATA || step == EventCreateStep.DONE

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            ArxTopBar(
                title = when (step) {
                    EventCreateStep.DATA -> "Nuevo evento"
                    EventCreateStep.CUTS -> "Selecciona los cortes"
                    EventCreateStep.PRICES -> "Asignar precios"
                    EventCreateStep.SUMMARY -> "Resumen"
                    EventCreateStep.DONE -> "¡Evento creado!"
                },
                navigationType = ArxTopBarNavigationType.BACK,
                onNavigationClick = { if (canLeaveDirectly) onBack() else viewModel.onBack() }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isCheckingRole || state.isSubmitting -> ArxLoading()
                !state.isAdmin -> ArxEmptyState(
                    title = "Sin permisos",
                    description = "Solo el propietario o un administrador puede crear eventos.",
                    iconRes = ArxIcons.Lock
                )
                else -> Column(Modifier.fillMaxSize()) {
                    StepIndicator(current = step.ordinal)

                    AnimatedContent(
                        targetState = step,
                        modifier = Modifier.weight(1f),
                        label = "event-create-step"
                    ) { target ->
                        when (target) {
                            EventCreateStep.DATA -> DataStep(
                                name = state.name,
                                dateMillis = state.dateMillis,
                                animalCount = state.animalCount,
                                onNameChange = viewModel::onNameChange,
                                onDateChange = viewModel::onDateChange,
                                onAnimalCountChange = viewModel::onAnimalCountChange
                            )
                            EventCreateStep.CUTS -> CutsStep(
                                cuts = state.visibleCuts,
                                catalogIsEmpty = state.cuts.isEmpty(),
                                selectedIds = state.selectedCutIds,
                                query = state.query,
                                isLoading = state.isLoadingCatalog,
                                onQueryChange = viewModel::onQueryChange,
                                onToggle = viewModel::onToggleCut
                            )
                            EventCreateStep.PRICES -> PricesStep(
                                cuts = state.selectedCuts,
                                prices = state.pricesByCutId,
                                onPriceChange = viewModel::onPriceChange
                            )
                            EventCreateStep.SUMMARY -> SummaryStep(state)
                            EventCreateStep.DONE -> DoneStep(state)
                        }
                    }

                    val action: Triple<String, Boolean, () -> Unit> = when (step) {
                        EventCreateStep.DATA -> Triple("Continuar", state.canContinueFromData, viewModel::onContinueFromData)
                        EventCreateStep.CUTS -> Triple(
                            if (state.selectedCutIds.isEmpty()) "Continuar"
                            else "Continuar (${state.selectedCutIds.size})",
                            state.canContinueFromCuts, viewModel::onContinueFromCuts
                        )
                        EventCreateStep.PRICES -> Triple("Continuar", state.canConfirmPrices, viewModel::onContinueFromPrices)
                        EventCreateStep.SUMMARY -> Triple("Crear evento", true, viewModel::onConfirm)
                        EventCreateStep.DONE -> Triple("Ir al evento", state.createdEventId != null) {
                            state.createdEventId?.let(onEventCreated)
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(ArxSpacing.md),
                        verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ArxPrimaryButton(text = action.first, enabled = action.second, onClick = action.third)
                        if (step == EventCreateStep.PRICES || step == EventCreateStep.SUMMARY) {
                            ArxSecondaryButton(text = "Atrás", onClick = viewModel::onBack)
                        }
                    }
                }
            }
        }
    }
}