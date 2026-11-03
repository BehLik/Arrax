package com.example.arrax.ui.screen.business.event

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.display.ArxEmptyState
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.designsystem.components.inputs.ArxQuantitySelector
import com.example.arrax.core.designsystem.components.inputs.ArxTextField
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIconBadge
import com.example.arrax.core.designsystem.icons.ArxIconShape
import com.example.arrax.core.designsystem.icons.ArxIconStyle
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.model.CutState
import com.example.arrax.domain.model.CutCatalog
import com.example.arrax.ui.screen.business.event.component.PriceInput
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private val StepLabels = listOf("Datos", "Cortes", "Precios", "Resumen", "Listo")

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

// ───────────────────────── Indicador de pasos ─────────────────────────

@Composable
private fun StepIndicator(current: Int) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.sm),
        horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
    ) {
        StepLabels.forEachIndexed { index, label ->
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(
                    Modifier.fillMaxWidth().height(4.dp).clip(MaterialTheme.shapes.small)
                        .background(if (index <= current) colors.primary else colors.outlineVariant)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (index <= current) colors.primary else colors.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StepHeader(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// ───────────────────────── Paso 1: Datos ─────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DataStep(
    name: String,
    dateMillis: Long?,
    animalCount: Int,
    onNameChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onAnimalCountChange: (Int) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }

    if (showPicker) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = dateMillis)
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let(onDateChange)
                    showPicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = { TextButton(onClick = { showPicker = false }) { Text("Cancelar") } }
        ) { DatePicker(state = pickerState) }
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(ArxSpacing.md),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
    ) {
        // Placeholder de la ilustración del cerdo 3D: reemplaza por tu drawable cuando lo tengas.
        ArxIconBadge(
            icon = ArxIcons.Event,
            style = ArxIconStyle.Soft,
            shape = ArxIconShape.Circle,
            containerSize = 72.dp,
            iconSize = 36.dp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        StepHeader("Nuevo evento", "Configura los detalles de tu venta porcina.")

        ArxTextField(value = name, onValueChange = onNameChange, label = "Nombre del evento")

        ArxCard(onClick = { showPicker = true }) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(ArxSpacing.md),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)
            ) {
                ArxIcon(ArxIcons.Date, tint = MaterialTheme.colorScheme.primary)
                Column {
                    Text("Fecha del evento", style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(dateMillis?.let(::formatDate) ?: "Selecciona una fecha",
                        style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Text("Cantidad de animales", style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        ArxQuantitySelector(
            value = animalCount.toDouble(),
            minValue = 1.0,
            onValueChange = { onAnimalCountChange(it.toInt()) }
        )
    }
}

// ───────────────────────── Paso 2: Cortes ─────────────────────────

@Composable
private fun CutsStep(
    cuts: List<CutCatalog>,
    catalogIsEmpty: Boolean,
    selectedIds: Set<String>,
    query: String,
    isLoading: Boolean,
    onQueryChange: (String) -> Unit,
    onToggle: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = ArxSpacing.md),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
    ) {
        StepHeader("Selecciona los cortes", "Elige los cortes que venderás en este evento.")
        ArxTextField(value = query, onValueChange = onQueryChange, label = "Buscar corte")
        Text(
            text = if (selectedIds.isEmpty()) "Puedes seleccionar varios cortes."
            else "${selectedIds.size} seleccionados",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        when {
            isLoading -> ArxLoading()
            catalogIsEmpty -> ArxEmptyState(
                title = "Sin cortes en el catálogo",
                description = "Agrega o activa cortes desde el catálogo del negocio.",
                iconRes = ArxIcons.Box
            )
            cuts.isEmpty() -> ArxEmptyState(
                title = "Sin resultados",
                description = "No hay cortes que coincidan con tu búsqueda.",
                iconRes = ArxIcons.SearchOff
            )
            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm),
                contentPadding = PaddingValues(bottom = ArxSpacing.md)
            ) {
                items(cuts, key = { it.id }) { cut ->
                    CutSelectCard(cut, selected = cut.id in selectedIds, onClick = { onToggle(cut.id) })
                }
            }
        }
    }
}

@Composable
private fun CutSelectCard(cut: CutCatalog, selected: Boolean, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val shape = MaterialTheme.shapes.medium
    Box(
        modifier = Modifier.then(
            if (selected) Modifier.border(2.dp, colors.primary, shape) else Modifier
        )
    ) {
        ArxCard(onClick = onClick) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(ArxSpacing.sm),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
            ) {
                CutImage(cut, Modifier.fillMaxWidth().aspectRatio(1f).clip(shape))
                Text(cut.name, style = MaterialTheme.typography.titleSmall, maxLines = 1)
                Text(
                    text = if (cut.state == CutState.COOKED) "Cocido" else "Fresco",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.onSurfaceVariant
                )
            }
        }
        if (selected) {
            ArxIconBadge(
                icon = ArxIcons.Check,
                style = ArxIconStyle.Solid,
                shape = ArxIconShape.Circle,
                containerSize = 24.dp,
                iconSize = 16.dp,
                modifier = Modifier.align(Alignment.TopEnd).padding(ArxSpacing.sm)
            )
        }
    }
}

/** Prioridad: imageUrl (subida por el negocio) → imageRes (drawable del dispositivo) → icono. */
@Composable
private fun CutImage(cut: CutCatalog, modifier: Modifier = Modifier) {
    val url = cut.imageUrl
    val res = cut.imageRes
    val fallback = res?.let { painterResource(it) }

    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        when {
            !url.isNullOrBlank() -> AsyncImage(
                model = url,
                contentDescription = cut.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = fallback,
                error = fallback
            )
            fallback != null -> Image(
                painter = fallback,
                contentDescription = cut.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            else -> ArxIcon(ArxIcons.Box, tint = MaterialTheme.colorScheme.onSurfaceVariant, size = 32.dp)
        }
    }
}

// ───────────────────────── Paso 3: Precios ─────────────────────────

@Composable
private fun PricesStep(
    cuts: List<CutCatalog>,
    prices: Map<String, PriceInput>,
    onPriceChange: (String, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(ArxSpacing.md),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
    ) {
        item {
            StepHeader("Asignar precios", "Ingresa el precio por kg para cada corte seleccionado.")
        }
        items(cuts, key = { it.id }) { cut ->
            ArxCard {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(ArxSpacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)
                ) {
                    CutImage(cut, Modifier.size(56.dp).clip(MaterialTheme.shapes.small))
                    Column(Modifier.weight(1f)) {
                        Text(cut.name, style = MaterialTheme.typography.titleSmall)
                        ArxTextField(
                            value = prices[cut.id]?.rawText.orEmpty(),
                            onValueChange = { onPriceChange(cut.id, it) },
                            label = "Precio por kg ($)",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    }
                }
            }
        }
        item {
            Text(
                "El precio se usará para calcular pedidos en kg o $.",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ───────────────────────── Paso 4: Resumen ─────────────────────────

@Composable
private fun SummaryStep(state: BusinessEventCreateState) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(ArxSpacing.md),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
    ) {
        StepHeader("Resumen", "Revisa que todo esté correcto antes de crear el evento.")

        ArxCard {
            Column(Modifier.fillMaxWidth().padding(ArxSpacing.md), verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)) {
                Text("Evento", style = MaterialTheme.typography.titleSmall)
                SummaryRow(ArxIcons.Event, "Nombre", state.name.trim())
                SummaryRow(ArxIcons.Date, "Fecha", state.dateMillis?.let(::formatDate).orEmpty())
                SummaryRow(ArxIcons.Box, "Animales", animalsLabel(state.animalCount))
            }
        }

        ArxCard {
            Column(Modifier.fillMaxWidth().padding(ArxSpacing.md), verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)) {
                Text("Precios por kg", style = MaterialTheme.typography.titleSmall)
                state.selectedCuts.forEach { cut ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(cut.name, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "${(state.priceCents(cut.id) ?: 0L).toMoney()}/kg",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(icon: Int, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)) {
        ArxIcon(icon, tint = MaterialTheme.colorScheme.primary, size = 20.dp)
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// ───────────────────────── Paso 5: Listo ─────────────────────────

@Composable
private fun DoneStep(state: BusinessEventCreateState) {
    Column(
        modifier = Modifier.fillMaxSize().padding(ArxSpacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.md, Alignment.CenterVertically)
    ) {
        ArxIconBadge(
            icon = ArxIcons.Check,
            style = ArxIconStyle.Solid,
            shape = ArxIconShape.Circle,
            containerSize = 96.dp,
            iconSize = 48.dp
        )
        Text("¡Evento creado!", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Tu evento está listo para comenzar a recibir pedidos.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        ArxCard {
            Column(Modifier.fillMaxWidth().padding(ArxSpacing.md)) {
                Text(state.name.trim(), style = MaterialTheme.typography.titleMedium)
                Text(
                    "${state.dateMillis?.let(::formatDate).orEmpty()} • ${animalsLabel(state.animalCount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ───────────────────────── Helpers ─────────────────────────

// Material3 DatePicker entrega medianoche UTC: se formatea en UTC para no mostrar un día menos.
private fun formatDate(millis: Long): String =
    SimpleDateFormat("EEE, dd MMM yyyy", Locale("es", "MX"))
        .apply { timeZone = TimeZone.getTimeZone("UTC") }
        .format(millis)
        .replaceFirstChar { it.uppercase() }

private fun animalsLabel(count: Int) = if (count == 1) "1 animal" else "$count animales"

private fun Long.toMoney(): String = String.format(Locale.US, "$%d.%02d", this / 100, this % 100)