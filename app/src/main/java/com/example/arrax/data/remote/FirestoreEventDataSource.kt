package com.example.arrax.data.remote


import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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
    private fun events(tenantId: String) =
        firestore.collection("tenants").document(tenantId).collection("eventos")

    fun observeEvents(tenantId: String): Flow<List<EventDto>> = callbackFlow {
        val listener = events(tenantId).addSnapshotListener { snapshot, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            trySend(snapshot?.toObjects<EventDto>().orEmpty())
        }
        awaitClose { listener.remove() }
    }

    suspend fun createEvent(tenantId: String, dto: EventDto): EventDto {
        val id = dto.id.ifBlank { UUID.randomUUID().toString() }
        val toSave = dto.copy(id = id)
        events(tenantId).document(id).set(toSave).await()
        return toSave
    }
}
