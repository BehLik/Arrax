
package com.example.arrax.data.remote

import android.util.Log
import com.example.arrax.core.common.FirestorePaths
import com.example.arrax.domain.model.EventStatus
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
    val id: String = "",
    val name: String = "",
    val date: Long = 0L,
    val animalCount: Int = 0,
    val status: String = EventStatus.OPEN.name,
    val createdBy: String = ""
)

@Singleton
class FirestoreEventDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    companion object {
        private const val TAG = "FirestoreEventDataSource"
    }

    private fun eventsCollection(tenantId: String) =
        firestore.collection(FirestorePaths.TENANTS)
            .document(tenantId)
            .collection(FirestorePaths.EVENTS)

    private fun cutsCollection(tenantId: String, eventId: String) =
        eventsCollection(tenantId)
            .document(eventId)
            .collection(FirestorePaths.EVENT_CUTS)

    // ---------------------------------------------------------
    // CREATE EVENT WITH CUTS
    // ---------------------------------------------------------

    suspend fun createEventWithCuts(
        tenantId: String,
        eventData: Map<String, Any>,
        cutsData: List<Map<String, Any>>
    ): String {

        Log.d(TAG, "========== CREATE EVENT START ==========")
        Log.d(TAG, "TenantId: $tenantId")
        Log.d(TAG, "Event data keys: ${eventData.keys}")
        Log.d(TAG, "Cuts received: ${cutsData.size}")

        try {
            val eventDoc = eventsCollection(tenantId).document()
            val batch = firestore.batch()

            Log.d(TAG, "Generated EventId: ${eventDoc.id}")

            // Create event document
            batch.set(
                eventDoc,
                eventData + ("id" to eventDoc.id)
            )

            Log.d(TAG, "Event document added to batch")

            // Create cuts documents
            cutsData.forEachIndexed { index, cut ->

                val cutId = cut["cutId"] as? String
                    ?: throw IllegalArgumentException(
                        "Cut at index $index is missing cutId"
                    )

                Log.d(TAG, "Adding cut [$index]: $cutId")

                val cutDoc = cutsCollection(
                    tenantId,
                    eventDoc.id
                ).document(cutId)

                batch.set(cutDoc, cut)
            }

            Log.d(TAG, "Committing batch...")

            batch.commit().await()

            Log.i(TAG, "EVENT CREATED SUCCESSFULLY")
            Log.i(TAG, "EventId: ${eventDoc.id}")
            Log.i(TAG, "Cuts saved: ${cutsData.size}")
            Log.d(TAG, "========== CREATE EVENT END ==========")

            return eventDoc.id

        } catch (e: Exception) {

            Log.e(
                TAG,
                "FAILED TO CREATE EVENT. TenantId: $tenantId",
                e
            )

            throw e
        }
    }

    // ---------------------------------------------------------
    // OBSERVE ALL EVENTS
    // ---------------------------------------------------------

    fun observeEvents(
        tenantId: String
    ): Flow<List<DocumentSnapshot>> = callbackFlow {

        Log.d(TAG, "========== OBSERVE EVENTS START ==========")
        Log.d(TAG, "TenantId: $tenantId")
        Log.d(TAG, "Listening to events collection...")

        val listener = eventsCollection(tenantId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {

                    Log.e(
                        TAG,
                        "ERROR OBSERVING EVENTS. TenantId: $tenantId",
                        error
                    )

                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot == null) {
                    Log.w(TAG, "Events snapshot is null")
                    return@addSnapshotListener
                }

                val events = snapshot.documents

                Log.d(TAG, "Events snapshot received")
                Log.d(TAG, "Events count: ${events.size}")
                Log.d(TAG, "Changes detected: ${snapshot.documentChanges.size}")

                events.forEach { document ->
                    Log.v(
                        TAG,
                        "Event: ${document.id} | " +
                                "Name: ${document.getString("name")} | " +
                                "Status: ${document.getString("status")}"
                    )
                }

                val result = trySend(events)

                if (result.isFailure) {
                    Log.e(TAG, "Failed to emit events: ${result.exceptionOrNull()}")
                }
            }

        Log.i(TAG, "Events listener registered")

        awaitClose {
            Log.d(TAG, "Removing events listener")
            listener.remove()
            Log.i(TAG, "Events listener removed")
        }
    }

    // ---------------------------------------------------------
    // OBSERVE SINGLE EVENT + CUTS
    // ---------------------------------------------------------

    fun observeEvent(
        tenantId: String,
        eventId: String
    ): Flow<Pair<DocumentSnapshot?, List<DocumentSnapshot>>> {

        Log.d(TAG, "========== OBSERVE EVENT START ==========")
        Log.d(TAG, "TenantId: $tenantId | EventId: $eventId")

        val eventFlow: Flow<DocumentSnapshot?> = callbackFlow {

            Log.d(TAG, "Registering event document listener: $eventId")

            val listener = eventsCollection(tenantId)
                .document(eventId)
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {

                        Log.e(
                            TAG,
                            "ERROR OBSERVING EVENT: $eventId",
                            error
                        )

                        close(error)
                        return@addSnapshotListener
                    }

                    if (snapshot == null) {
                        Log.w(TAG, "Event snapshot is null: $eventId")
                        return@addSnapshotListener
                    }

                    Log.d(TAG, "Event snapshot received")
                    Log.d(TAG, "Event exists: ${snapshot.exists()}")
                    Log.d(TAG, "Event data: ${snapshot.data}")

                    val result = trySend(snapshot)

                    if (result.isFailure) {
                        Log.e(
                            TAG,
                            "Failed to emit event: ${result.exceptionOrNull()}"
                        )
                    }
                }

            Log.i(TAG, "Event listener registered: $eventId")

            awaitClose {
                Log.d(TAG, "Removing event listener: $eventId")
                listener.remove()
            }
        }

        val cutsFlow: Flow<List<DocumentSnapshot>> = callbackFlow {

            Log.d(TAG, "Registering cuts listener for event: $eventId")

            val listener = cutsCollection(tenantId, eventId)
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {

                        Log.e(
                            TAG,
                            "ERROR OBSERVING CUTS. EventId: $eventId",
                            error
                        )

                        close(error)
                        return@addSnapshotListener
                    }

                    if (snapshot == null) {
                        Log.w(TAG, "Cuts snapshot is null: $eventId")
                        return@addSnapshotListener
                    }

                    val cuts = snapshot.documents

                    Log.d(TAG, "Cuts snapshot received")
                    Log.d(TAG, "Cuts count: ${cuts.size}")

                    cuts.forEach { cut ->
                        Log.v(
                            TAG,
                            "Cut: ${cut.id} | Data: ${cut.data}"
                        )
                    }

                    val result = trySend(cuts)

                    if (result.isFailure) {
                        Log.e(
                            TAG,
                            "Failed to emit cuts: ${result.exceptionOrNull()}"
                        )
                    }
                }

            Log.i(TAG, "Cuts listener registered: $eventId")

            awaitClose {
                Log.d(TAG, "Removing cuts listener: $eventId")
                listener.remove()
            }
        }

        return eventFlow.combine(cutsFlow) { eventSnap, cutSnaps ->

            Log.d(TAG, "Combining event and cuts data")
            Log.d(TAG, "Event available: ${eventSnap != null}")
            Log.d(TAG, "Cuts available: ${cutSnaps.size}")

            eventSnap to cutSnaps
        }
    }

    // ---------------------------------------------------------
    // CLOSE EVENT
    // ---------------------------------------------------------

    suspend fun closeEvent(
        tenantId: String,
        eventId: String
    ) {

        Log.d(TAG, "========== CLOSE EVENT START ==========")
        Log.d(TAG, "TenantId: $tenantId")
        Log.d(TAG, "EventId: $eventId")

        try {

            eventsCollection(tenantId)
                .document(eventId)
                .update(
                    "status",
                    EventStatus.CLOSED.name
                )
                .await()

            Log.i(TAG, "EVENT CLOSED SUCCESSFULLY")
            Log.i(TAG, "EventId: $eventId")
            Log.i(TAG, "New status: ${EventStatus.CLOSED.name}")

        } catch (e: Exception) {

            Log.e(
                TAG,
                "FAILED TO CLOSE EVENT: $eventId",
                e
            )

            throw e
        }

        Log.d(TAG, "========== CLOSE EVENT END ==========")
    }
}
