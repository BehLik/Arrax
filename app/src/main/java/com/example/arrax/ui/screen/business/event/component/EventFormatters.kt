package com.example.arrax.ui.screen.business.event.component

import java.util.Locale

 fun animalsLabel(count: Int) = if (count == 1) "1 animal" else "$count animales"

 fun Long.toMoney(): String = String.format(Locale.US, "$%d.%02d", this / 100, this % 100)