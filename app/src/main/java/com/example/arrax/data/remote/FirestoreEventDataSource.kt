package com.example.arrax.data.remote


import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

data class EventDto(
    @DocumentId val id: String = "",
    val name: String = "",
    val date: Long = 0L,
    val pigCount: Int = 0,
    val status: String = "OPEN",
    val createdBy: String = ""
)

@Singleton
class FirestoreEventDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private fun eventsCollection(tenantId: String) =
        firestore.collection("tenants").document(tenantId).collection("events")

    // Nombre de subcoleccion en ingles para mantener consistencia con el resto del
    // esquema (tenants/events/users). La spec de Fase 3 la llama "cortesEvento" -
    // ajusta este nombre si ya tienes reglas de seguridad de Firestore escritas
    // contra ese nombre en espanol.
    private fun cutsCollection(tenantId: String, eventId: String) =
        eventsCollection(tenantId).document(eventId).collection("eventCuts")

    // Escritura atomica: crea el documento del evento y un documento por cada corte
    // seleccionado en la subcoleccion eventCuts. Todo o nada (write batch).
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

    // Combina el listener del documento del evento con el de su subcoleccion de cortes,
    // para que EventDetail reaccione tanto a cambios de estado como de precio/cortes.
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
            .update("status", "CLOSED")
            .await()
    }
}
