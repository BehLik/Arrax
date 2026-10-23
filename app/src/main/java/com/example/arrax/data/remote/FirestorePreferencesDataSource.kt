package com.example.arrax.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestorePreferencesDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    // TODO: mover estas constantes a FirestorePaths / FirestoreFields (no vi sus nombres exactos).
    private companion object {
        const val USERS = "users"
        const val PREFERENCES = "preferences"
    }

    /** Emite el mapa crudo `preferences` (o null si no hay sesión o aún no existe). */
    fun observe(): Flow<Map<String, Any?>?> = callbackFlow {
        var docReg: ListenerRegistration? = null

        val authListener = FirebaseAuth.AuthStateListener { a ->
            docReg?.remove()
            docReg = null
            val uid = a.currentUser?.uid
            if (uid == null) {
                trySend(null)
            } else {
                docReg = firestore.collection(USERS).document(uid)
                    .addSnapshotListener { snap, error ->
                        if (error != null) {
                            close(error)
                            return@addSnapshotListener
                        }
                        @Suppress("UNCHECKED_CAST")
                        trySend(snap?.get(PREFERENCES) as? Map<String, Any?>)
                    }
            }
        }
        auth.addAuthStateListener(authListener)

        awaitClose {
            auth.removeAuthStateListener(authListener)
            docReg?.remove()
        }
    }

    /** Merge profundo sobre `preferences`; no se espera la confirmación del servidor. */
    fun merge(patch: Map<String, Any>) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection(USERS).document(uid)
            .set(mapOf(PREFERENCES to patch), SetOptions.merge())
    }
}