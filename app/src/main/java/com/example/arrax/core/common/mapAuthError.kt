package com.example.arrax.core.common

/**
 * Traduce excepciones de Firebase Auth a mensajes en español, no técnicos,
 * para mostrar directo en la UI. `fallback` es el mensaje genérico que ya
 * trae Resource.Error cuando no se reconoce el tipo de excepción.
 * Se apoya en AppError: la clasificación de excepciones vive en un solo lugar.
 */
fun mapAuthError(throwable: Throwable?, fallback: String): String =
    when (throwable?.toAppError()) {
        is AppError.InvalidCredentials -> "Correo o contraseña incorrectos."
        is AppError.UserNotFound -> "No existe una cuenta con ese correo."
        is AppError.EmailInUse -> "Ya existe una cuenta con ese correo."
        is AppError.WeakPassword -> "La contraseña es demasiado débil, usa al menos 6 caracteres."
        is AppError.Network -> "No hay conexión a internet. Intenta de nuevo."
        is AppError.EmailNotVerified -> "Verifica tu correo para continuar."
        else -> fallback
    }
