package com.example.arrax.domain.repository

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.TenantNotification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun observeAdminNotifications(uid: String): Flow<List<TenantNotification>>
    suspend fun markRead(tenantId: String, id: String, uid: String): Resource<Unit>
}