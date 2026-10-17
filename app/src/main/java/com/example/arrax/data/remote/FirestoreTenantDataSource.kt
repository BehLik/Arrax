package com.example.arrax.data.remote

import com.example.arrax.core.common.FirestorePaths
import com.example.arrax.domain.model.Role
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
    val role: String = Role.OPERATOR.name
)

/**
 * Firestore is both remote and local (no Room): the SDK keeps a persistent local cache and syncs it,
 * so reads use addSnapshotListener wrapped in callbackFlow instead of one-off .get().
 */
@Singleton
class FirestoreTenantDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private fun tenants() = firestore.collection(FirestorePaths.TENANTS)
    private fun users() = firestore.collection(FirestorePaths.USERS)

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

    // Atomic (batch) and merge only tenantId/role: writing a whole UserDto with merge
    // would overwrite the existing "name" with "".
    suspend fun createTenant(name: String, adminUid: String): TenantDto {
        val tenantId = UUID.randomUUID().toString()
        val dto = TenantDto(id = tenantId, name = name, createdAt = System.currentTimeMillis())
        val batch = firestore.batch()
        batch.set(tenants().document(tenantId), dto)
        batch.set(
            users().document(adminUid),
            mapOf("tenantId" to tenantId, "role" to Role.ADMIN.name),
            SetOptions.merge()
        )
        batch.commit().await()
        return dto
    }

    // The invitation code is assumed equal to tenantId for now; the real invitation model
    // (own codes, expiry, assigned role) is left for a later phase.
    suspend fun joinTenant(invitationCode: String, uid: String): TenantDto {
        val snapshot = tenants().document(invitationCode).get().await()
        val dto = snapshot.toObject<TenantDto>() ?: error("Código de invitación inválido")
        users().document(uid)
            .set(mapOf("tenantId" to dto.id, "role" to Role.OPERATOR.name), SetOptions.merge())
            .await()
        return dto
    }
}
