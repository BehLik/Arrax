package com.example.arrax.ui.screen.auth.google

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

private const val TAG = "GoogleSignIn"

sealed interface GoogleTokenResult {
    data class Success(val idToken: String) : GoogleTokenResult
    data object Cancelled : GoogleTokenResult
    data class Failure(val message: String) : GoogleTokenResult
}

suspend fun requestGoogleIdToken(context: Context, serverClientId: String): GoogleTokenResult {
    val request = GetCredentialRequest.Builder()
        .addCredentialOption(GetSignInWithGoogleOption.Builder(serverClientId).build())
        .build()
    return try {
        val credential = CredentialManager.create(context).getCredential(context, request).credential
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            GoogleTokenResult.Success(GoogleIdTokenCredential.createFrom(credential.data).idToken)
        } else {
            Log.e(TAG, "Tipo de credencial inesperado: ${credential.type}")
            GoogleTokenResult.Failure("Credencial no compatible")
        }
    } catch (e: GetCredentialCancellationException) {
        Log.d(TAG, "Cancelado: type=${e.type} msg=${e.message}", e)
        GoogleTokenResult.Cancelled
    } catch (e: NoCredentialException) {
        Log.e(TAG, "NoCredentialException: ${e.message}", e)
        GoogleTokenResult.Failure("No hay una cuenta de Google disponible en este dispositivo")
    } catch (e: GetCredentialException) {
        Log.e(TAG, "GetCredentialException type=${e.type} msg=${e.message}", e)
        GoogleTokenResult.Failure("No se pudo iniciar sesión con Google")
    } catch (e: GoogleIdTokenParsingException) {
        Log.e(TAG, "Token inválido", e)
        GoogleTokenResult.Failure("Respuesta de Google inválida")
    }
}