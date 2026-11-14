package com.example.arrax.ui.screen.events.orders.subscreen.make

import com.example.arrax.core.designsystem.components.cards.ArxCatalogImage
import com.example.arrax.domain.model.Customer
import com.example.arrax.domain.model.EventCut
import com.example.arrax.domain.model.OrderMath
import com.example.arrax.domain.model.RequestMode

data class OrderLineUi(
    val cutId: String,
    val cutName: String,
    val pricePerKgCents: Long,
    val mode: RequestMode = RequestMode.KG,
    val grams: Int = 1000,
    val amountCents: Long = 0L,
) {
    val targetGrams: Int
        get() = if (mode == RequestMode.KG) grams
        else OrderMath.gramsFromAmount(amountCents, pricePerKgCents)

    val estimatedCents: Long
        get() = if (mode == RequestMode.MONEY) amountCents
        else OrderMath.amountFromGrams(grams, pricePerKgCents)
}

data class EventMakeOrderState(
    val isLoading: Boolean = true,
    val eventOpen: Boolean = true,
    val customers: List<Customer> = emptyList(),
    val eventCuts: List<EventCut> = emptyList(),
    val cutImages: Map<String, ArxCatalogImage> = emptyMap(),
    val selectedCustomerId: String? = null,
    val lines: List<OrderLineUi> = emptyList(),
    val notes: String = "",
    val showCutPicker: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val saved: Boolean = false,
    val showSummary: Boolean = false,
    val infoMessage: String? = null,

) {
    val confirmHint: String?
        get() = when {
            !eventOpen -> null
            selectedCustomerId == null -> "Elige un cliente para continuar"
            lines.isEmpty() -> "Agrega al menos un corte"
            lines.any { it.targetGrams <= 0 } -> "Falta la cantidad en algún corte"
            else -> null
        }
    fun imageOf(cutId: String): ArxCatalogImage = cutImages[cutId] ?: ArxCatalogImage.None
    val selectedCustomer: Customer? get() = customers.firstOrNull { it.id == selectedCustomerId }

    // Cortes del evento que aún no están en el pedido
    val pickableCuts: List<EventCut>
        get() = eventCuts.filter { cut -> lines.none { it.cutId == cut.cutId } }

    val totalCents: Long get() = lines.sumOf { it.estimatedCents }

    val canConfirm: Boolean
        get() = eventOpen && !isSaving && selectedCustomerId != null &&
                lines.isNotEmpty() && lines.all { it.targetGrams > 0 }
}