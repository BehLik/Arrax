package com.example.arrax.core.common

import java.text.NumberFormat
import java.util.Locale

private val mxCurrency: NumberFormat =
    NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"))

fun Long.toMoney(): String = synchronized(mxCurrency) { mxCurrency.format(this / 100.0) }