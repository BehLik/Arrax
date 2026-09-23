package com.example.arrax.core.common

/**
 * Envoltura uniforme de estado para todo ViewModel del proyecto:
 * permite que la UI reaccione a loading/success/error sin lógica repetida por pantalla.
 */
sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : Resource<Nothing>()
}
