package com.example.arrax.data.repository


import com.example.arrax.core.common.Resource
import com.example.arrax.data.remote.FirestoreNotificationDataSource
import com.example.arrax.data.remote.FirestoreTenantDataSource
import com.example.arrax.data.remote.NotificationDto
import com.example.arrax.domain.model.NotificationType
import com.example.arrax.domain.model.Role
import com.example.arrax.domain.model.TenantNotification
import com.example.arrax.domain.repository.NotificationRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val tenantDataSource: FirestoreTenantDataSource,
    private val source: FirestoreNotificationDataSource
) : NotificationRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeAdminNotifications(uid: String): Flow<List<TenantNotification>> =
        tenantDataSource.observeMyMemberships(uid).flatMapLatest { memberships ->
            val adminTenants = memberships.filter {
                it.role == Role.ADMIN.name || it.role == Role.OWNER.name
            }
            if (adminTenants.isEmpty()) flowOf(emptyList())
            else combine(
                adminTenants.map { m ->
                    // un negocio con error no debe tumbar a los demás
                    source.observe(m.tenantId).catch { emit(emptyList()) }
                }
            ) { lists -> lists.toList().flatten() }
                .map { all ->
                    all.map { it.toDomain(uid) }.sortedByDescending { it.createdAt }
                }
        }

    override suspend fun markRead(tenantId: String, id: String, uid: String): Resource<Unit> = try {
        source.markRead(tenantId, id, uid)
        Resource.Success(Unit)
    } catch (e: CancellationException) { throw e
    } catch (e: Exception) { Resource.Error(e.message ?: "No se pudo marcar como leída", e) }
}

private fun NotificationDto.toDomain(uid: String) = TenantNotification(
    id = id,
    tenantId = tenantId,
    tenantName = tenantName,
    type = runCatching { NotificationType.valueOf(type) }.getOrDefault(NotificationType.UNKNOWN),
    actorName = actorName,
    role = runCatching { Role.valueOf(role) }.getOrDefault(Role.OPERATOR),
    createdAt = createdAt,
    isRead = uid in readBy
)