package com.example.arrax.core.common
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale
/** Money is stored as integer cents; BigDecimal only for display (no floating point). */
object Money {
    private val defaultLocale: Locale = Locale.forLanguageTag("es-MX")

    fun format(cents: Long, locale: Locale = defaultLocale): String =
        NumberFormat.getCurrencyInstance(locale).format(BigDecimal.valueOf(cents, 2))
}