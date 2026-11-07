package com.example.arrax.data.remote

import android.util.Log
import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.FirestorePaths
import com.example.arrax.domain.model.DefaultCuts
import com.example.arrax.domain.model.Role
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
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
data class MembershipDto(
    val tenantId: String,
    val role: String
)
data class MemberDto(
    val uid: String = "",
    val name: String = "",
    val role: String = Role.OPERATOR.name,
    val joinedAt: Long = 0L,
    val inviteCode: String? = null
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

    fun observeMyMemberships(uid: String): Flow<List<MembershipDto>> = callbackFlow {
        val listener = firestore.collectionGroup(FirestorePaths.MEMBERS)
            .whereEqualTo("uid", uid)
            .addSnapshotListener { snap, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                val list = snap?.documents.orEmpty().mapNotNull { doc ->
                    val tenantId = doc.reference.parent.parent?.id ?: return@mapNotNull null
                    MembershipDto(tenantId, doc.getString("role") ?: Role.OPERATOR.name)
                }
                trySend(list)
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
    suspend fun joinTenant(invitationCode: String, uid: String, userName: String): TenantDto = try {
        // Descarta el token: refresca y muestra email_verified
        val token = FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.await()
        Log.d("QR_JOIN", "uid=$uid code=$invitationCode email_verified=${token?.claims?.get("email_verified")}")

        firestore.runTransaction { tx ->
            val inviteRef = firestore.collection(FirestorePaths.INVITES).document(invitationCode)
            val now = System.currentTimeMillis()

            Log.d("QR_JOIN", "1. leyendo invite")
            val invite = tx.get(inviteRef).toObject<InviteDto>() ?: throw AppError.InviteNotFound()
            Log.d("QR_JOIN", "1. invite OK: tenantId=${invite.tenantId} used=${invite.usedCount}/${invite.maxUses} revoked=${invite.revoked} expiresAt=${invite.expiresAt} now=$now")
            if (invite.revoked || invite.expiresAt <= now) throw AppError.InviteExpired()
            if (invite.usedCount >= invite.maxUses) throw AppError.InviteFull()

            val memberRef = members(invite.tenantId).document(uid)
            Log.d("QR_JOIN", "2. leyendo member")
            if (tx.get(memberRef).exists()) throw AppError.AlreadyMember()
            Log.d("QR_JOIN", "2. member OK (no existe)")

            Log.d("QR_JOIN", "3. leyendo tenant")
            val tenant = tx.get(tenants().document(invite.tenantId)).toObject<TenantDto>()
                ?: throw AppError.InviteNotFound()
            Log.d("QR_JOIN", "3. tenant OK: ${tenant.id}")

            Log.d("QR_JOIN", "4. preparando escrituras (se envían al hacer commit)")
            tx.update(inviteRef, "usedCount", invite.usedCount + 1)
            tx.set(
                memberRef,
                mapOf(
                    "uid" to uid,
                    "role" to Role.OPERATOR.name,
                    "name" to userName,
                    "joinedAt" to now,
                    "inviteCode" to invitationCode
                )
            )
            tx.set(
                users().document(uid),
                mapOf("tenantId" to tenant.id, "role" to Role.OPERATOR.name),
                SetOptions.merge()
            )
            tx.set(
                tenants().document(invite.tenantId)
                    .collection(FirestorePaths.NOTIFICATIONS).document(),
                mapOf(
                    "type" to "MEMBER_JOINED",
                    "tenantId" to invite.tenantId,
                    "tenantName" to tenant.name,
                    "actorUid" to uid,
                    "actorName" to userName,
                    "role" to Role.OPERATOR.name,
                    "inviteCode" to invitationCode,
                    "createdAt" to now,
                    "readBy" to emptyList<String>()
                )
            )
            Log.d("QR_JOIN", "5. escrituras listas, falta commit")
            tenant
        }.await().also { Log.d("QR_JOIN", "6. COMMIT OK") }
    } catch (e: Exception) {
        Log.e("QR_JOIN", "joinTenant falló: ${e::class.simpleName}: ${e.message}", e)
        throw generateSequence<Throwable>(e) { it.cause }.firstOrNull { it is AppError } ?: e
    }
    fun observeMembers(tenantId: String): Flow<List<MemberDto>> = callbackFlow {
        val listener = members(tenantId)
            .orderBy("joinedAt")
            .addSnapshotListener { snap, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                trySend(snap?.documents.orEmpty().mapNotNull { it.toObject<MemberDto>() })
            }
        awaitClose { listener.remove() }
    }
    // FirestoreTenantDataSource
    fun observeMyRole(tenantId: String, uid: String): Flow<Role> = callbackFlow {
        val l = members(tenantId).document(uid).addSnapshotListener { snap, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            trySend(
                snap?.getString("role")
                    ?.let { runCatching { Role.valueOf(it) }.getOrNull() } ?: Role.NONE
            )
        }
        awaitClose { l.remove() }
    }
}
