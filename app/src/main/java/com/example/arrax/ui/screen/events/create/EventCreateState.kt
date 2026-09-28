package com.example.arrax.ui.screen.events.create


import com.example.arrax.domain.model.CutCatalog

data class EventCreateState(
    val step: Int = 0, // 0 = datos basicos, 1 = seleccion de cortes, 2 = precios
    val isAdmin: Boolean = false,
    val isCheckingRole: Boolean = true,

    // Paso 1
    val name: String = "",
    val dateMillis: Long? = null,
    val pigCount: Int = 1,

    // Paso 2
    val availableCuts: List<CutCatalog> = emptyList(),
    val selectedCutIds: Set<String> = emptySet(),
    val isLoadingCatalog: Boolean = false,

    // Paso 3
    val pricesByCutId: Map<String, String> = emptyMap(), // texto crudo, se valida al confirmar

    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val createdEventId: String? = null
) {
    val canContinueFromBasicData: Boolean
        get() = name.isNotBlank() && dateMillis != null && pigCount >= 1

    val selectedCuts: List<CutCatalog>
        get() = availableCuts.filter { it.id in selectedCutIds }

    val canContinueFromCuts: Boolean
        get() = selectedCutIds.isNotEmpty()

    val canConfirm: Boolean
        get() = selectedCutIds.isNotEmpty() && selectedCutIds.all { cutId ->
            pricesByCutId[cutId]?.toDoubleOrNull()?.let { it > 0.0 } ?: false
        }
}
