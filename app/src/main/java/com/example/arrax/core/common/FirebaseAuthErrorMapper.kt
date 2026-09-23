package com.example.arrax.core.common

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

/**
 * Traduce excepciones de Firebase Auth a mensajes en español, no técnicos,
 * para mostrar directo en la UI. `fallback` es el mensaje genérico que ya
 * trae Resource.Error cuando no se reconoce el tipo de excepción.
 */
fun mapAuthError(throwable: Throwable?, fallback: String): String = when (throwable) {
    is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos."
    is FirebaseAuthInvalidUserException -> "No existe una cuenta con ese correo."
    is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con ese correo."
    is FirebaseAuthWeakPasswordException -> "La contraseña es demasiado débil, usa al menos 6 caracteres."
    is FirebaseNetworkException -> "No hay conexión a internet. Intenta de nuevo."
    else -> fallback
}
