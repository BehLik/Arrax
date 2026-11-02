package com.example.arrax.data.remote

import com.example.arrax.core.common.FirestorePaths
import com.example.arrax.core.model.DefaultCuts
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

data class CutCatalogDto(
    @DocumentId val id: String = "",
    val name: String = "",
    val state: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val active: Boolean = true,
)

@Singleton
class FirestoreCatalogDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private fun catalog(tenantId: String) =
        firestore.collection(FirestorePaths.TENANTS)
            .document(tenantId)
            .collection(FirestorePaths.CUT_CATALOG)

    fun observeCatalog(tenantId: String): Flow<List<CutCatalogDto>> = callbackFlow {
        val listener = catalog(tenantId).addSnapshotListener { snapshot, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            trySend(snapshot?.toObjects<CutCatalogDto>().orEmpty())
        }
        awaitClose { listener.remove() }
    }

    // Seeds DefaultCuts using the stable English id as document id (idempotent and matches
    // CutOption.id), and the Spanish display name as the stored name.
    fun newId(tenantId: String): String = catalog(tenantId).document().id

    suspend fun upsert(tenantId: String, dto: CutCatalogDto) {
        catalog(tenantId).document(dto.id).set(dto).await()
    }

    suspend fun delete(tenantId: String, cutId: String) {
        catalog(tenantId).document(cutId).delete().await()
    }

    suspend fun seedIfEmpty(tenantId: String) {
        val existing = catalog(tenantId).get().await()
        if (!existing.isEmpty) return
        val batch = firestore.batch()
        DefaultCuts.forEach { cut ->
            batch.set(
                catalog(tenantId).document(cut.id),
                CutCatalogDto(cut.id, cut.name, cut.state.name, cut.description, null, true)
            )
        }
        batch.commit().await()
    }
}
