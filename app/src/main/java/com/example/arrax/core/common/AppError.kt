package com.example.arrax.core.common

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestoreException

/** Typed errors for repositories/use cases. The UI maps each one to a string resource. */
sealed class AppError(cause: Throwable? = null) : Exception(cause) {
    class Network(cause: Throwable? = null) : AppError(cause)
    class InvalidCredentials(cause: Throwable? = null) : AppError(cause)
    class UserNotFound(cause: Throwable? = null) : AppError(cause)
    class EmailInUse(cause: Throwable? = null) : AppError(cause)
    class PermissionDenied(cause: Throwable? = null) : AppError(cause)
    class Validation(val field: String) : AppError()
    class Unknown(cause: Throwable? = null) : AppError(cause)
}

private const val TAG = "AppError"

fun Throwable.toAppError(): AppError = when (this) {
    is AppError -> this
    is FirebaseNetworkException -> AppError.Network(this)
    is FirebaseAuthInvalidCredentialsException -> AppError.InvalidCredentials(this)
    is FirebaseAuthInvalidUserException -> AppError.UserNotFound(this)
    is FirebaseAuthUserCollisionException -> AppError.EmailInUse(this)
    is FirebaseFirestoreException -> when (code) {
        FirebaseFirestoreException.Code.PERMISSION_DENIED -> AppError.PermissionDenied(this)
        FirebaseFirestoreException.Code.UNAVAILABLE -> AppError.Network(this)
        else -> AppError.Unknown(this).also { Log.e(TAG, "Firestore error: $code", this) }
    }
    is FirebaseAuthException -> AppError.Unknown(this).also { Log.e(TAG, "Auth error: $errorCode", this) }
    else -> AppError.Unknown(this).also { Log.e(TAG, "Unexpected error", this) }
}
