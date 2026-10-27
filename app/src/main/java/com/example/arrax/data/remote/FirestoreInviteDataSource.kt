package com.example.arrax.data.remote

import com.example.arrax.core.common.FirestoreFields
import com.example.arrax.core.common.FirestorePaths
import com.example.arrax.core.common.InviteCode
import com.example.arrax.domain.model.Role
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

data class InviteDto(
    @DocumentId val code: String = "",
    val tenantId: String = "",
    val tenantName: String = "",
    val role: String = Role.OPERATOR.name,
    val expiresAt: Long = 0L,
    val revoked: Boolean = false,
    val createdBy: String = "",
    val createdAt: Long = 0L
)

@Singleton
class FirestoreInviteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun createInvite(tenantId: String, createdBy: String, ttlMillis: Long): InviteDto {
        val tenantName = firestore.collection(FirestorePaths.TENANTS).document(tenantId)
            .get().await().getString(FirestoreFields.NAME).orEmpty()
        val now = System.currentTimeMillis()
        val code = InviteCode.generate()
        val dto = InviteDto(
            code = code, tenantId = tenantId, tenantName = tenantName,
            expiresAt = now + ttlMillis, createdBy = createdBy, createdAt = now
        )
        firestore.collection(FirestorePaths.INVITES).document(code).set(dto).await()
        return dto
    }
}