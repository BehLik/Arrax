package com.example.arrax.domain.model
enum class NotificationType { MEMBER_JOINED, UNKNOWN }

data class TenantNotification(
    val id: String,
    val tenantId: String,
    val tenantName: String,
    val type: NotificationType,
    val actorName: String,
    val role: Role,
    val createdAt: Long,
    val isRead: Boolean
)