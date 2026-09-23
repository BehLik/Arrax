package com.example.arrax.data.remote


import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

data class TenantDto(
    @DocumentId val id: String = "",
    val name: String = "",
    val createdAt: Long = 0L
)

data class UserDto(
    @DocumentId val uid: String = "",
    val name: String = "",
    val tenantId: String? = null,
    val role: String = "OPERADOR"
)

/**
 * Nota: "Firestore como remoto y local" -- no usamos Room. El SDK de Firestore
 * ya mantiene caché local persistente y la sincroniza solo; por eso los reads
 * se hacen con addSnapshotListener envuelto en callbackFlow y no con .get() suelto.
 */
@Singleton
class FirestoreTenantDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private fun tenants() = firestore.collection("tenants")
    private fun users() = firestore.collection("usuarios")

    fun observeUser(uid: String): Flow<UserDto?> = callbackFlow {
        val listener = users().document(uid).addSnapshotListener { snapshot, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            trySend(snapshot?.toObject<UserDto>())
        }
        awaitClose { listener.remove() }
    }

    fun observeTenant(tenantId: String): Flow<TenantDto?> = callbackFlow {
        val listener = tenants().document(tenantId).addSnapshotListener { snapshot, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            trySend(snapshot?.toObject<TenantDto>())
        }
        awaitClose { listener.remove() }
    }

    suspend fun createTenant(name: String, adminUid: String): TenantDto {
        val tenantId = UUID.randomUUID().toString()
        val dto = TenantDto(id = tenantId, name = name, createdAt = System.currentTimeMillis())
        tenants().document(tenantId).set(dto).await()
        users().document(adminUid)
            .set(UserDto(uid = adminUid, tenantId = tenantId, role = "ADMIN"), SetOptions.merge())
            .await()
        return dto
    }

    // El código de invitación se asume igual al tenantId por ahora; el modelo real
    // de invitaciones (códigos propios, expiración, rol asignado) queda para una fase posterior.
    suspend fun joinTenant(invitationCode: String, uid: String): TenantDto {
        val snapshot = tenants().document(invitationCode).get().await()
        val dto = snapshot.toObject<TenantDto>() ?: error("Código de invitación inválido")
        users().document(uid)
            .set(UserDto(uid = uid, tenantId = dto.id, role = "OPERADOR"), SetOptions.merge())
            .await()
        return dto
    }
}
