package com.example.arrax.ui.screen.events.create

import androidx.compose.runtime.Immutable
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Parsed once, when the text changes (in the ViewModel) - not in a getter on every recomposition.
 * Usage in the ViewModel:
 *   onPriceChange(cutId, raw) = update { it.copy(prices = it.prices + (cutId to PriceInput.from(raw))) }
 *   canConfirm can stay a getter: it only reads isValid, no parsing.
 */
@Immutable
data class PriceInput(
    val rawText: String = "",
    val cents: Long? = null,
    val isError: Boolean = false
) {
    val isValid: Boolean get() = cents != null && !isError

    companion object {
        private const val MAX_LENGTH = 9

        fun from(raw: String): PriceInput {
            val text = raw.trim().replace(',', '.').take(MAX_LENGTH)
            if (text.isEmpty()) return PriceInput(rawText = "")
            val value = text.toBigDecimalOrNull()
                ?: return PriceInput(rawText = text, isError = true)
            if (value <= BigDecimal.ZERO) return PriceInput(rawText = text, isError = true)
            val cents = value.setScale(2, RoundingMode.HALF_UP).movePointRight(2).toLong()
            return PriceInput(rawText = text, cents = cents)
        }
    }
}
