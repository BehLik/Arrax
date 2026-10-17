package com.example.arrax.ui.screen.events.create

import androidx.compose.runtime.Immutable
import com.example.arrax.domain.model.CutCatalog

@Immutable
data class EventCreateState(
    val step: Int = 0, // 0 = basic data, 1 = cut selection, 2 = prices
    val isAdmin: Boolean = false,
    val isCheckingRole: Boolean = true,

    // Step 1
    val name: String = "",
    val dateMillis: Long? = null,
    val pigCount: Int = 1,

    // Step 2
    val availableCuts: List<CutCatalog> = emptyList(),
    val selectedCutIds: Set<String> = emptySet(),
    val isLoadingCatalog: Boolean = false,

    // Step 3 - parsed once in the ViewModel (see PriceInput)
    val pricesByCutId: Map<String, PriceInput> = emptyMap(),

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

    // No string parsing here anymore: only reads the already-validated PriceInput.
    val canConfirm: Boolean
        get() = selectedCutIds.isNotEmpty() &&
                selectedCutIds.all { pricesByCutId[it]?.isValid == true }
}
