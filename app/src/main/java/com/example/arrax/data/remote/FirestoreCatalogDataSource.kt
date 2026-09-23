package com.example.arrax.data.remote


import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.
toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

data class CutCatalogDto(
    @DocumentId val id: String = "",
    val name: String = "",
    val active: Boolean = true
)

@Singleton
class FirestoreCatalogDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private fun catalog(tenantId: String) =
        firestore.collection("tenants").document(tenantId).collection("catalogoCortes")

    // Semilla de 9 cortes conocidos (incluye "cabeza").
    val defaultCuts = listOf(
        "carne", "costilla", "espinazo", "codillo", "chicharra",
        "higadilla", "morcilla", "manteca", "cabeza"
    )

    fun observeCatalog(tenantId: String): Flow<List<CutCatalogDto>> = callbackFlow {
        val listener = catalog(tenantId).addSnapshotListener { snapshot, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            trySend(snapshot?.toObjects<CutCatalogDto>().orEmpty())
        }
        awaitClose { listener.remove() }
    }

    suspend fun seedIfEmpty(tenantId: String) {
        val existing = catalog(tenantId).get().await()
        if (!existing.isEmpty) return
        val batch = firestore.batch()
        defaultCuts.forEach { cutName ->
            val ref = catalog(tenantId).document()
            batch.set(ref, CutCatalogDto(id = ref.id, name = cutName, active = true))
        }
        batch.commit().await()
    }
}
