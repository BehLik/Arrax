package com.example.arrax.data.remote

import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.FirestorePaths
import com.example.arrax.core.model.DefaultCuts
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
    val ownerUid: String = "",
    val createdAt: Long = 0L,
    val eventCount: Long = 0L
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
    private fun members(tenantId: String) =
        tenants().document(tenantId).collection(FirestorePaths.MEMBERS)

    private fun cutCatalog(tenantId: String) =
        tenants().document(tenantId).collection(FirestorePaths.CUT_CATALOG)

    /** Atómico: tenant + member OWNER + seed de cutCatalog (9 cortes, muy por debajo del límite de 500). */
    suspend fun createTenant(name: String, ownerUid: String, ownerName: String): TenantDto {
        val tenantId = firestore.collection(FirestorePaths.TENANTS).document().id // id autogenerado
        val now = System.currentTimeMillis()
        val tenantRef = tenants().document(tenantId)
        val dto = TenantDto(id = tenantId, name = name, ownerUid = ownerUid, createdAt = now, eventCount = 0)

        val batch = firestore.batch()
        batch.set(tenantRef, dto)
        batch.set(
            tenantRef.collection(FirestorePaths.MEMBERS).document(ownerUid),
            mapOf(
                "uid" to ownerUid,
                "role" to Role.OWNER.name,
                "name" to ownerName,
                "joinedAt" to now
            )
        )
        DefaultCuts.forEach { cut ->
            batch.set(
                tenantRef.collection(FirestorePaths.CUT_CATALOG).document(cut.id),
                buildMap<String, Any> {
                    put("name", cut.name)
                    put("state", cut.state.name)
                    put("active", true)
                    cut.description?.let { put("description", it) }
                }
            )
        }
        batch.commit().await()
        return dto
    }

    // The invitation code is assumed equal to tenantId for now; the real invitation model
    // (own codes, expiry, assigned role) is left for a later phase.
    suspend fun joinTenant(invitationCode: String, uid: String): TenantDto {
        val inviteSnap = firestore.collection(FirestorePaths.INVITES) // agrega INVITES = "invites"
            .document(invitationCode).get().await()
        if (!inviteSnap.exists()) throw throw AppError.InviteNotFound()

        val tenantId = inviteSnap.getString("tenantId") ?: throw AppError.InviteNotFound()
        val revoked = inviteSnap.getBoolean("revoked") ?: false
        val expiresAt = inviteSnap.getLong("expiresAt") ?: 0L
        if (revoked || expiresAt < System.currentTimeMillis()) throw AppError.InviteExpired()

        val memberRef = members(tenantId).document(uid)
        if (memberRef.get().await().exists()) throw throw AppError.AlreadyMember()

        val tenantSnap = tenants().document(tenantId).get().await()
        val dto = tenantSnap.toObject<TenantDto>() ?: throw AppError.InviteNotFound()

        val userName = users().document(uid).get().await().getString("name").orEmpty()

        // Atómico: miembro (lo que exigen las reglas) + negocio activo del usuario
        val batch = firestore.batch()
        batch.set(
            memberRef,
            mapOf(
                "uid" to uid,
                "role" to Role.OPERATOR.name,
                "name" to userName,
                "joinedAt" to System.currentTimeMillis()
            )
        )
        batch.set(
            users().document(uid),
            mapOf("tenantId" to dto.id, "role" to Role.OPERATOR.name),
            SetOptions.merge()
        )
        batch.commit().await()
        return dto
    }
}
