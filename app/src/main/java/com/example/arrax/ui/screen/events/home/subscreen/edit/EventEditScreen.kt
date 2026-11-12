package com.example.arrax.ui.screen.events.home.subscreen.edit

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.arrax.ui.screen.events.home.subscreen.edit.component.EventEditCutsStep

@Composable
fun EventEditScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventEditViewmodel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) { if (state.isSaved) onSaved() }

    EventEditContent(
        state = state,
        onBackClick = { if (state.step == EventEditStep.DATA) onBack() else viewModel.onBack() },
        onNameChange = viewModel::onNameChange,
        onDateChange = viewModel::onDateChange,
        onAnimalCountChange = viewModel::onAnimalCountChange,
        onQueryChange = viewModel::onQueryChange,
        onToggleCut = viewModel::onToggleCut,
        onPriceChange = viewModel::onPriceChange,
        onRemoveCut = viewModel::onRemoveCut,
        onContinueFromData = viewModel::onContinueFromData,
        onOpenPicker = viewModel::onOpenPicker,
        onClosePicker = viewModel::onClosePicker,
        onSaveClick = viewModel::onSave,
        onBackStepClick = viewModel::onBack,
        onErrorShown = viewModel::onErrorShown,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EventEditContent(
    state: EventEditState,
    onBackClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onAnimalCountChange: (Int) -> Unit,
    onQueryChange: (String) -> Unit,
    onToggleCut: (String) -> Unit,
    onPriceChange: (String, String) -> Unit,
    onRemoveCut: (String) -> Unit,
    onContinueFromData: () -> Unit,
    onOpenPicker: () -> Unit,
    onClosePicker: () -> Unit,
    onSaveClick: () -> Unit,
    onBackStepClick: () -> Unit,
    onErrorShown: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackbar.showSnackbar(it)
            onErrorShown()
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            ArxTopBar(
                title = when (state.step) {
                    EventEditStep.DATA -> "Editar evento"
                    EventEditStep.CUTS -> "Cortes del evento"
                    EventEditStep.PICK -> "Agregar cortes"
                },
                navigationType = ArxTopBarNavigationType.BACK,
                onNavigationClick = onBackClick
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading || state.canEdit == null || state.isSaving -> ArxLoading()
                state.canEdit == false -> ArxEmptyState(
                    title = "Sin permisos",
                    description = "Solo el propietario o un administrador puede editar eventos.",
                    iconRes = ArxIcons.Lock
                )
                else -> Column(Modifier.fillMaxSize().imePadding()) {
                    AnimatedContent(
                        targetState = state.step,
                        modifier = Modifier.weight(1f),
                        label = "event-edit-step"
                    ) { target ->
                        when (target) {
                            EventEditStep.DATA -> DataStep(
                                name = state.name,
                                dateMillis = state.dateMillis,
                                animalCount = state.animalCount,
                                onNameChange = onNameChange,
                                onDateChange = onDateChange,
                                onAnimalCountChange = onAnimalCountChange
                            )
                            EventEditStep.CUTS -> EventEditCutsStep(
                                cuts = state.cuts,
                                catalog = state.catalog,
                                onPriceChange = onPriceChange,
                                onRemoveCut = onRemoveCut,
                                onAddCutsClick = onOpenPicker
                            )
                            EventEditStep.PICK -> CutsStep(
                                cuts = state.visibleCatalog,
                                catalogIsEmpty = state.catalog.isEmpty(),
                                selectedIds = state.selectedIds,
                                query = state.query,
                                isLoading = state.isLoadingCatalog,
                                onQueryChange = onQueryChange,
                                onToggle = onToggleCut
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth().padding(ArxSpacing.md),
                        verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (state.step) {
                            EventEditStep.DATA -> ArxPrimaryButton(
                                text = "Continuar",
                                enabled = state.canContinueFromData,
                                onClick = onContinueFromData
                            )
                            EventEditStep.CUTS -> {
                                ArxPrimaryButton(
                                    text = "Guardar cambios",
                                    enabled = state.cuts.isNotEmpty(),
                                    onClick = onSaveClick
                                )
                                ArxSecondaryButton(text = "Atrás", onClick = onBackStepClick)
                            }
                            EventEditStep.PICK -> ArxPrimaryButton(
                                text = "Listo (${state.selectedIds.size})",
                                onClick = onClosePicker
                            )
                        }
                    }
                }
            }
        }
    }
}