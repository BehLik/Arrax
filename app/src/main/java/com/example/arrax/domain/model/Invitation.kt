package com.example.arrax.domain.model

enum class InviteType { CODE_QR, CODE, QR }

data class Invitation(
    val code: String,
    val tenantId: String,
    val tenantName: String,
    val role: Role,
    val type: InviteType,
    val maxUses: Int,
    val usedCount: Int,
    val createdAt: Long,
    val expiresAt: Long,
    val revoked: Boolean
)
enum class InviteStatus { ACTIVE, FULL, EXPIRED, REVOKED }

fun Invitation.status(now: Long) = when {
    revoked -> InviteStatus.REVOKED
    expiresAt <= now -> InviteStatus.EXPIRED
    usedCount >= maxUses -> InviteStatus.FULL
    else -> InviteStatus.ACTIVE
}