package com.example.arrax.data.remote

import com.example.arrax.core.common.FirestoreFields
import com.example.arrax.core.common.FirestorePaths
import com.example.arrax.core.common.InviteCode
import com.example.arrax.domain.model.Invitation
import com.example.arrax.domain.model.InviteType
import com.example.arrax.domain.model.Role
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

data class InviteDto(
    @DocumentId val code: String = "",
    val tenantId: String = "",
    val tenantName: String = "",
    val role: String = Role.OPERATOR.name,
    val type: String = InviteType.CODE_QR.name,
    val maxUses: Int = 1,
    val usedCount: Int = 0,
    val expiresAt: Long = 0L,
    val revoked: Boolean = false,
    val createdBy: String = "",
    val createdAt: Long = 0L
)
fun InviteDto.toDomain() = Invitation(
    code = code,
    tenantId = tenantId,
    tenantName = tenantName,
    role = runCatching { Role.valueOf(role) }.getOrDefault(Role.OPERATOR),
    type = runCatching { InviteType.valueOf(type) }.getOrDefault(InviteType.CODE_QR),
    maxUses = maxUses,
    usedCount = usedCount,
    createdAt = createdAt,
    expiresAt = expiresAt,
    revoked = revoked
)

@Singleton
class FirestoreInviteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private fun invites() = firestore.collection(FirestorePaths.INVITES)

    suspend fun createInvite(
        tenantId: String,
        createdBy: String,
        role: Role,
        type: InviteType,
        maxUses: Int,
        validDays: Int
    ): InviteDto {
        val tenantName = firestore.collection(FirestorePaths.TENANTS).document(tenantId)
            .get().await().getString(FirestoreFields.NAME).orEmpty()
        val now = System.currentTimeMillis()
        val code = InviteCode.generate()
        val dto = InviteDto(
            code = code,
            tenantId = tenantId,
            tenantName = tenantName,
            role = role.name,
            type = type.name,
            maxUses = maxUses,
            usedCount = 0,
            expiresAt = now + validDays * 86_400_000L,
            revoked = false,
            createdBy = createdBy,
            createdAt = now
        )
        invites().document(code).set(dto).await()
        return dto
    }

    fun observeInvites(tenantId: String): Flow<List<Invitation>> = callbackFlow {
        val listener = invites()
            .whereEqualTo(FirestoreFields.TENANT_ID, tenantId)
            .addSnapshotListener { snap, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                trySend(snap?.documents.orEmpty().mapNotNull { it.toObject<InviteDto>()?.toDomain() })
            }
        awaitClose { listener.remove() }
    }
}