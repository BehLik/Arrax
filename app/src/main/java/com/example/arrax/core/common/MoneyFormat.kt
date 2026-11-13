package com.example.arrax.core.common

import java.text.NumberFormat
import java.util.Locale

fun Long.toMoney(): String =
    NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(this / 100.0)