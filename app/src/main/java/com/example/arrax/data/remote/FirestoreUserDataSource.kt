package com.example.arrax.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreUserDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val users get() = firestore.collection("users")

    // Sigue el uid autenticado actual y escucha /users/{uid} en tiempo real.
    // Sin sesion emite null; si el uid cambia (login/logout) cambia de listener solo.
    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeCurrentUserDoc(): Flow<DocumentSnapshot?> {
        return authUidFlow().flatMapLatest { uid ->
            if (uid.isNullOrBlank()) flowOf(null) else observeUserDoc(uid)
        }
    }

    private fun authUidFlow(): Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser?.uid) }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    private fun observeUserDoc(uid: String): Flow<DocumentSnapshot?> = callbackFlow {
        val listener = users.document(uid).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot)
        }
        awaitClose { listener.remove() }
    }
}
