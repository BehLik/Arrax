package com.example.arrax.data.remote

import com.example.arrax.core.common.FirestorePaths
import com.example.arrax.domain.model.EventStatus
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

data class EventDto(
    @DocumentId val id: String = "",
    val name: String = "",
    val date: Long = 0L,
    val pigCount: Int = 0,
    val status: String = EventStatus.OPEN.name,
    val createdBy: String = ""
)

@Singleton
class FirestoreEventDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private fun eventsCollection(tenantId: String) =
        firestore.collection(FirestorePaths.TENANTS)
            .document(tenantId)
            .collection(FirestorePaths.EVENTS)

    // Subcollection name is fixed to "eventCuts" (English); firebase/firestore.rules matches it.
    private fun cutsCollection(tenantId: String, eventId: String) =
        eventsCollection(tenantId).document(eventId).collection(FirestorePaths.EVENT_CUTS)

    // Atomic write: the event document plus one document per selected cut (write batch).
    suspend fun createEventWithCuts(
        tenantId: String,
        eventData: Map<String, Any>,
        cutsData: List<Map<String, Any>>
    ): String {
        val eventDoc = eventsCollection(tenantId).document()
        val batch = firestore.batch()

        batch.set(eventDoc, eventData + ("id" to eventDoc.id))

        cutsData.forEach { cut ->
            val cutId = cut["cutId"] as? String
                ?: error("Cada corte necesita cutId antes de escribirse")
            val cutDoc = cutsCollection(tenantId, eventDoc.id).document(cutId)
            batch.set(cutDoc, cut)
        }

        batch.commit().await()
        return eventDoc.id
    }

    fun observeEvents(tenantId: String): Flow<List<DocumentSnapshot>> = callbackFlow {
        val listener = eventsCollection(tenantId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.documents.orEmpty())
            }
        awaitClose { listener.remove() }
    }

    // Combines the event document listener with its cuts subcollection listener so
    // EventDetail reacts to both status and price/cut changes.
    fun observeEvent(
        tenantId: String,
        eventId: String
    ): Flow<Pair<DocumentSnapshot?, List<DocumentSnapshot>>> {
        val eventFlow: Flow<DocumentSnapshot?> = callbackFlow {
            val listener = eventsCollection(tenantId).document(eventId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    trySend(snapshot)
                }
            awaitClose { listener.remove() }
        }

        val cutsFlow: Flow<List<DocumentSnapshot>> = callbackFlow {
            val listener = cutsCollection(tenantId, eventId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    trySend(snapshot?.documents.orEmpty())
                }
            awaitClose { listener.remove() }
        }

        return eventFlow.combine(cutsFlow) { eventSnap, cutSnaps -> eventSnap to cutSnaps }
    }

    suspend fun closeEvent(tenantId: String, eventId: String) {
        eventsCollection(tenantId).document(eventId)
            .update("status", EventStatus.CLOSED.name)
            .await()
    }
}
