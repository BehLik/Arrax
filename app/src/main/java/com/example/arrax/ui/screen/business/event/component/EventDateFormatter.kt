package com.example.arrax.ui.screen.business.event.component

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun formatDate(millis: Long): String =
    SimpleDateFormat("EEE, dd MMM yyyy", Locale("es", "MX"))
        .apply { timeZone = TimeZone.getTimeZone("UTC") }
        .format(millis)
        .replaceFirstChar { it.uppercase()
        }