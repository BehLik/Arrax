package com.example.arrax.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthDataSource @Inject constructor(
    private val auth: FirebaseAuth
) {
    fun observeAuthState(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun login(email: String, password: String): FirebaseUser {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user ?: error("No se pudo iniciar sesión")
    }

    suspend fun signInWithGoogle(idToken: String): FirebaseUser {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        return result.user ?: error("No se pudo iniciar sesión con Google")
    }

    suspend fun register(email: String, password: String, name: String): FirebaseUser {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: error("No se pudo crear el usuario")
        user.updateProfile(userProfileChangeRequest { displayName = name }).await()
        return user
    }

    suspend fun sendPasswordReset(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }
    // FirebaseAuthDataSource
    suspend fun sendEmailVerification() {
        val user = auth.currentUser ?: error("Sin sesión activa")
        user.sendEmailVerification().await()
    }

    /** Refresca el usuario y el token (para que las reglas vean email_verified). */
    suspend fun reloadAndCheckVerified(): Boolean {
        val user = auth.currentUser ?: error("Sin sesión activa")
        user.reload().await()
        if (user.isEmailVerified) user.getIdToken(true).await()
        return user.isEmailVerified
    }

    suspend fun deleteCurrentUser() {
        auth.currentUser?.delete()?.await()
    }

    fun currentEmail(): String? = auth.currentUser?.email
    fun isCurrentEmailVerified(): Boolean = auth.currentUser?.isEmailVerified == true
    fun logout() = auth.signOut()

    fun currentUid(): String? = auth.currentUser?.uid
}
