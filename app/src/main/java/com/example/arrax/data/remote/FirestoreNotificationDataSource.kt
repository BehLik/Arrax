package com.example.arrax.data.remote

import com.example.arrax.core.common.FirestorePaths
import com.example.arrax.domain.model.Role
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

data class NotificationDto(
    @DocumentId val id: String = "",
    val type: String = "",
    val tenantId: String = "",
    val tenantName: String = "",
    val actorName: String = "",
    val role: String = Role.OPERATOR.name,
    val createdAt: Long = 0L,
    val readBy: List<String> = emptyList()
)

@Singleton
class FirestoreNotificationDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private fun col(tenantId: String) = firestore.collection(FirestorePaths.TENANTS)
        .document(tenantId).collection(FirestorePaths.NOTIFICATIONS)

    fun observe(tenantId: String): Flow<List<NotificationDto>> = callbackFlow {
        val listener = col(tenantId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snap, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                trySend(snap?.documents.orEmpty().mapNotNull { it.toObject<NotificationDto>() })
            }
        awaitClose { listener.remove() }
    }

    suspend fun markRead(tenantId: String, id: String, uid: String) {
        col(tenantId).document(id).update("readBy", FieldValue.arrayUnion(uid)).await()
    }
}