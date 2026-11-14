package com.example.arrax.ui.screen.events.orders.subscreen.make

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.core.common.toMoney
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.inputs.ArxTextField
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.components.order.ArxOrderSummaryCard
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.ui.screen.events.orders.subscreen.make.component.CustomerSelector
import com.example.arrax.ui.screen.events.orders.subscreen.make.component.CutPickerSheet
import com.example.arrax.ui.screen.events.orders.subscreen.make.component.OrderSectionHeader
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.components.order.ArxOrderLineEditCard
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.domain.model.Customer
import com.example.arrax.domain.model.EventCut
import com.example.arrax.domain.model.RequestMode
import com.example.arrax.ui.screen.events.orders.subscreen.make.component.OrderConfirmSheet
import java.util.Locale

@Composable
fun EventMakeOrderScreen(
    onBack: () -> Unit,
    onSelectCustomer: () -> Unit,
    selectedCustomerId: String?,
    onSelectedCustomerConsumed: () -> Unit,
    viewModel: EventMakeOrderViewModel = hiltViewModel(),
){
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.saved) { if (state.saved) onBack() }
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onErrorShown()
        }
    }
    LaunchedEffect(selectedCustomerId) {
        selectedCustomerId?.let {
            viewModel.onCustomerSelected(it)
            onSelectedCustomerConsumed()
        }
    }
    LaunchedEffect(state.infoMessage) {
        state.infoMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onInfoShown()
        }
    }

    EventMakeOrderContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onSelectCustomer = onSelectCustomer,
        onAddCut = viewModel::onAddCutClick,
        onModeChange = viewModel::onModeChange,
        onGramsChange = viewModel::onGramsChange,
        onAmountChange = viewModel::onAmountChange,
        onRemoveLine = viewModel::onRemoveLine,
        onNotesChange = viewModel::onNotesChange,
        onConfirm = viewModel::onConfirmClick,
    )

    if (state.showCutPicker) {
        CutPickerSheet(
            cuts = state.pickableCuts,
            onPick = viewModel::onCutPicked,
            imageOf = state::imageOf,
            onDismiss = viewModel::onDismissCutPicker,
        )
    }
    if (state.showSummary) {
        OrderConfirmSheet(
            customerName = state.selectedCustomer?.name.orEmpty(),
            notes = state.notes,
            lines = state.lines,
            isSaving = state.isSaving,
            onConfirm = { viewModel.onSave(addAnother = false) },
            onConfirmAndAddAnother = { viewModel.onSave(addAnother = true) },
            onDismiss = viewModel::onDismissSummary,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventMakeOrderContent(
    state: EventMakeOrderState,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onSelectCustomer: () -> Unit,
    onAddCut: () -> Unit,
    onModeChange: (String, RequestMode) -> Unit,
    onGramsChange: (String, Int) -> Unit,
    onAmountChange: (String, Long) -> Unit,
    onRemoveLine: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onConfirm: () -> Unit,
) {
    ArxBackground {
        Scaffold(
            modifier = Modifier.imePadding(),
            containerColor = Color.Transparent,
            topBar = {
                ArxTopBar(
                    title = "Hacer pedido",
                    navigationType = ArxTopBarNavigationType.BACK,
                    onNavigationClick = onBack
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    ArxPrimaryButton(
                        text = if (state.isSaving) "Guardando…" else "Confirmar pedido",
                        onClick = onConfirm,
                        enabled = state.canConfirm,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                    )
                }
            },
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp, end = 16.dp,
                    top = padding.calculateTopPadding() + 8.dp,
                    bottom = padding.calculateBottomPadding() + 16.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (!state.isLoading && !state.eventOpen) {
                    item { Text("El evento está cerrado: ya no se pueden crear pedidos.") }
                }

                item {
                    OrderSectionHeader(title = "Cliente", iconRes = ArxIcons.User)
                }
                item {
                    CustomerSelector(
                        selected = state.selectedCustomer,
                        onClick = onSelectCustomer,
                    )
                }

                item {
                    OrderSectionHeader(
                        title = "Cortes",
                        iconRes = ArxIcons.Meat,
                        actionText = "Agregar",
                        actionIconRes = ArxIcons.Add,
                        onAction = onAddCut,
                    )
                }
                if (state.lines.isEmpty()) {
                    item { Text("Agrega al menos un corte para continuar.") }
                } else {
                    items(state.lines, key = { it.cutId }) { line ->
                        val money = line.mode == RequestMode.MONEY
                        ArxOrderLineEditCard(
                            title = line.cutName,
                            image = state.imageOf(line.cutId),
                            pricePerKgText = "${line.pricePerKgCents.toMoney()}/kg",
                            totalText = line.estimatedCents.toMoney(),
                            isMoneyMode = money,
                            grams = line.grams,
                            amountCents = line.amountCents,
                            equivalentText = if (money) "≈ ${String.format(Locale.US, "%.3f", line.targetGrams / 1000.0)} kg" else null,
                            onModeChange = { m -> onModeChange(line.cutId, if (m) RequestMode.MONEY else RequestMode.KG) },
                            onGramsChange = { onGramsChange(line.cutId, it) },
                            onAmountCentsChange = { onAmountChange(line.cutId, it) },
                            onRemove = { onRemoveLine(line.cutId) },
                        )
                    }
                }

                item {
                    ArxOrderSummaryCard(
                        title = "Total del pedido",
                        totalText = state.totalCents.toMoney(),
                        caption = "Estimado. Se cobra por el peso real al pesar.",
                    )
                }

                item { OrderSectionHeader(title = "Notas (opcional)") }
                item {
                    ArxTextField(
                        value = state.notes,
                        onValueChange = onNotesChange,
                        label = "Ej. Poca grasa, pierna o lomo…",

                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun EventMakeOrderScreenPreview() {
    ArxTheme {
        EventMakeOrderContent(
            state = EventMakeOrderState(
                isLoading = false,
                eventOpen = true,
                customers = listOf(
                    Customer(id = "1", name = "María López", phone = "+52 771 987 6543", createdAt = 0L),
                    Customer(
                        id = "2",
                        name = "Juan Pérez",
                        phone = "+52 771 123 4567",
                        createdAt = 0L
                    ),
                ),
                selectedCustomerId = "1",
                eventCuts = listOf(
                    EventCut(cutId = "ribs", name = "Costilla", pricePerKgCents = 7_500L),
                    EventCut(cutId = "lard", name = "Manteca", pricePerKgCents = 4_000L),
                    EventCut(cutId = "loin", name = "Lomo", pricePerKgCents = 9_000L),
                ),
                lines = listOf(
                    OrderLineUi(cutId = "ribs", cutName = "Costilla", pricePerKgCents = 7_500L, grams = 1_500),
                    OrderLineUi(cutId = "lard", cutName = "Manteca", pricePerKgCents = 4_000L, grams = 500),
                ),
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onBack = {},
            onSelectCustomer = {},
            onAddCut = {},
//            onQuantityChange = { _, _ -> },
            onRemoveLine = {},
            onNotesChange = {},
            onConfirm = {},
            onModeChange = { _, _ -> },
            onGramsChange = { _, _ -> },
            onAmountChange = { _, _ -> },
        )
    }
}