package com.example.arrax.core.common


import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/** Emite la hora actual ahora y cada [periodMs]; refresca etiquetas como "Expira en 3 días". */
fun minuteTicker(periodMs: Long = 60_000L): Flow<Long> = flow {
    while (true) {
        emit(System.currentTimeMillis())
        delay(periodMs)
    }
}