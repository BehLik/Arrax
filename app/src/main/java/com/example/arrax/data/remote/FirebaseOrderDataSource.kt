package com.example.arrax.data.remote

import android.util.Log
import com.example.arrax.core.common.FirestorePaths
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

data class OrderDto(
    @DocumentId val id: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val notes: String = "",
    val createdAt: Long = 0L,
    val createdBy: String = "",
)

data class OrderItemDto(
    @DocumentId val id: String = "",
    val orderId: String = "",
    val customerId: String = "",
    val cutId: String = "",
    val cutName: String = "",
    val pricePerKgCents: Long = 0L,
    val requestedBy: String = "KG",
    val requestedGrams: Int? = null,
    val requestedAmountCents: Long? = null,
    val targetGrams: Int = 0,
    val weighedGrams: Int = 0,
    val weighingStatus: String = "PENDING",
    val deliveredAt: Long? = null,
    val createdAt: Long = 0L,
)

@Singleton
class FirestoreOrderDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private fun event(t: String, e: String) =
        firestore.collection(FirestorePaths.TENANTS).document(t)
            .collection(FirestorePaths.EVENTS).document(e)

    private fun orders(t: String, e: String) = event(t, e).collection(FirestorePaths.ORDERS)
    private fun items(t: String, e: String) = event(t, e).collection(FirestorePaths.ORDER_ITEMS)

    fun newId(t: String, e: String): String = orders(t, e).document().id

    fun observeOrders(t: String, e: String): Flow<List<OrderDto>> = callbackFlow {
        val l = orders(t, e).addSnapshotListener { snap, err ->
            if (err != null) { close(err); return@addSnapshotListener }
            trySend(snap?.toObjects<OrderDto>().orEmpty())
        }
        awaitClose { l.remove() }
    }

    fun observeItems(t: String, e: String): Flow<List<OrderItemDto>> = callbackFlow {
        val l = items(t, e).addSnapshotListener { snap, err ->
            if (err != null) { close(err); return@addSnapshotListener }
            trySend(snap?.toObjects<OrderItemDto>().orEmpty())
        }
        awaitClose { l.remove() }
    }

    // Batch sin await: offline se encola y sincroniza sola. Pedido + líneas, todo o nada.
    fun createOrder(t: String, e: String, order: OrderDto, lines: List<OrderItemDto>) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: error("No hay sesión")
        val now = System.currentTimeMillis()
        val batch = firestore.batch()
        batch.set(orders(t, e).document(order.id), order.copy(createdAt = now, createdBy = uid))
        lines.forEach { line ->
            // id determinista: evita dos líneas del mismo corte en un pedido
            val itemId = "${order.id}_${line.cutId}"
            batch.set(items(t, e).document(itemId), line.copy(orderId = order.id, createdAt = now))
        }
        batch.commit().addOnFailureListener { Log.e("ORDERS", "createOrder falló", it) }
    }

    fun deleteOrder(t: String, e: String, orderId: String) {
        // Borra el pedido y sus líneas juntos.
        items(t, e).whereEqualTo("orderId", orderId).get().addOnSuccessListener { snap ->
            val batch = firestore.batch()
            snap.documents.forEach { batch.delete(it.reference) }
            batch.delete(orders(t, e).document(orderId))
            batch.commit().addOnFailureListener { Log.e("ORDERS", "deleteOrder falló", it) }
        }
    }

    fun confirmWeighing(t: String, e: String, itemId: String, grams: Int, status: String) {
        items(t, e).document(itemId)
            .update(mapOf("weighedGrams" to grams, "weighingStatus" to status))
            .addOnFailureListener { Log.e("ORDERS", "confirmWeighing falló", it) }
    }

    fun markDelivered(t: String, e: String, itemIds: List<String>, deliveredAt: Long?) {
        val batch = firestore.batch()
        itemIds.forEach { batch.update(items(t, e).document(it), "deliveredAt", deliveredAt) }
        batch.commit().addOnFailureListener { Log.e("ORDERS", "markDelivered falló", it) }
    }
    fun deleteOrder(t: String, e: String, orderId: String, itemIds: List<String>) {
        val batch = firestore.batch()
        itemIds.forEach { batch.delete(items(t, e).document(it)) }
        batch.delete(orders(t, e).document(orderId))
        batch.commit().addOnFailureListener { Log.e("ORDERS", "deleteOrder falló", it) }
    }

    fun updateOrder(
        t: String, e: String, orderId: String, notes: String,
        added: List<OrderItemDto>, updated: List<OrderItemDto>, removedIds: Set<String>,
    ) {
        val now = System.currentTimeMillis()
        val batch = firestore.batch()
        batch.update(orders(t, e).document(orderId), "notes", notes.trim())
        added.forEach { batch.set(items(t, e).document(it.id), it.copy(createdAt = now)) }
        updated.forEach {
            batch.update(items(t, e).document(it.id), mapOf(
                "requestedBy" to it.requestedBy,
                "requestedGrams" to it.requestedGrams,
                "requestedAmountCents" to it.requestedAmountCents,
                "targetGrams" to it.targetGrams,
                "pricePerKgCents" to it.pricePerKgCents,
            ))
        }
        removedIds.forEach { batch.delete(items(t, e).document(it)) }
        batch.commit().addOnFailureListener { Log.e("ORDERS", "updateOrder falló", it) }
    }

}