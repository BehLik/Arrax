package com.example.arrax.ui.screen.business.event

import androidx.compose.runtime.Immutable
import com.example.arrax.domain.model.CutCatalog
import com.example.arrax.ui.screen.business.event.component.PriceInput

enum class EventCreateStep { DATA, CUTS, PRICES, SUMMARY, DONE }

@Immutable
data class BusinessEventCreateState(
    val step: EventCreateStep = EventCreateStep.DATA,
    val isCheckingRole: Boolean = true,
    val isAdmin: Boolean = false,
    val tenantId: String? = null,

    // Paso 1 - Datos
    val name: String = "",
    val dateMillis: Long? = null, // UTC (así lo entrega Material3 DatePicker)
    val animalCount: Int = 1,

    // Paso 2 - Cortes (solo activos del catálogo del negocio)
    val cuts: List<CutCatalog> = emptyList(),
    val isLoadingCatalog: Boolean = true,
    val query: String = "",
    val selectedCutIds: Set<String> = emptySet(),

    // Paso 3 - Precios (parseados una vez en el ViewModel)
    val pricesByCutId: Map<String, PriceInput> = emptyMap(),

    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val createdEventId: String? = null,
) {
    val canContinueFromData: Boolean
        get() = name.isNotBlank() && dateMillis != null && animalCount >= 1

    val visibleCuts: List<CutCatalog>
        get() = if (query.isBlank()) cuts
        else cuts.filter { it.name.contains(query.trim(), ignoreCase = true) }

    // Se calcula sobre todo el catálogo (no sobre el filtro) para no perder selección al buscar.
    val selectedCuts: List<CutCatalog>
        get() = cuts.filter { it.id in selectedCutIds }

    val canContinueFromCuts: Boolean
        get() = selectedCutIds.isNotEmpty()

    val canConfirmPrices: Boolean
        get() = selectedCutIds.isNotEmpty() &&
                selectedCutIds.all { pricesByCutId[it]?.isValid == true }

    fun priceCents(cutId: String): Long? = pricesByCutId[cutId]?.cents
}