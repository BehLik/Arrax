package com.example.arrax.data.remote

import android.util.Log
import com.example.arrax.core.common.FirestorePaths
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

data class CustomerDto(
    @DocumentId val id: String = "",
    val name: String = "",
    val phone: String? = null,
    val address: String? = null,
    val reference: String? = null,
    val photoUrl: String? = null,
    val lastDeliveryLocation: GeoPoint? = null,
    val lastDeliveryAt: Long? = null,
    val createdAt: Long = 0L,
    val createdBy: String = "",
)

@Singleton
class FirestoreCustomerDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private fun customers(tenantId: String) =
        firestore.collection(FirestorePaths.TENANTS)
            .document(tenantId)
            .collection(FirestorePaths.CUSTOMERS)

    fun observeCustomers(tenantId: String): Flow<List<CustomerDto>> = callbackFlow {
        val listener = customers(tenantId).addSnapshotListener { snap, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            trySend(snap?.toObjects<CustomerDto>().orEmpty())
        }
        awaitClose { listener.remove() }
    }

    fun observeCustomer(tenantId: String, customerId: String): Flow<CustomerDto?> = callbackFlow {
        val listener = customers(tenantId).document(customerId)
            .addSnapshotListener { snap, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                trySend(snap?.toObject<CustomerDto>())
            }
        awaitClose { listener.remove() }
    }

    fun newId(tenantId: String): String = customers(tenantId).document().id

    // Sin await: offline se encola y sincroniza sola.
    fun create(tenantId: String, dto: CustomerDto) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: error("No hay sesión")
        customers(tenantId).document(dto.id)
            .set(dto.copy(createdAt = System.currentTimeMillis(), createdBy = uid))
            .addOnFailureListener { Log.e("CUSTOMERS", "create falló", it) }
    }

    // update parcial: un set() con el DTO completo borraría lastDeliveryLocation/At.
    fun update(tenantId: String, dto: CustomerDto) {
        val fields = buildMap<String, Any?> {
            put("name", dto.name)
            put("phone", dto.phone)
            put("address", dto.address)
            put("reference", dto.reference)
            dto.photoUrl?.let { put("photoUrl", it) } // null = sin cambio
        }
        customers(tenantId).document(dto.id)
            .update(fields)
            .addOnFailureListener { Log.e("CUSTOMERS", "update falló", it) }
    }

    fun delete(tenantId: String, customerId: String) {
        customers(tenantId).document(customerId)
            .delete()
            .addOnFailureListener { Log.e("CUSTOMERS", "delete falló", it) }
    }
}