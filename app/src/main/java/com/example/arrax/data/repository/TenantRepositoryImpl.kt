package com.example.arrax.data.repository

import com.example.arrax.core.common.Resource
import com.example.arrax.data.local.SessionLocalDataSource
import com.example.arrax.data.mapper.toDomain
import com.example.arrax.data.remote.FirestoreTenantDataSource
import com.example.arrax.domain.model.Role
import com.example.arrax.domain.model.Tenant
import com.example.arrax.domain.model.TenantMembership
import com.example.arrax.domain.repository.TenantRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TenantRepositoryImpl @Inject constructor(
    private val tenantDataSource: FirestoreTenantDataSource,
    private val sessionLocalDataSource: SessionLocalDataSource
) : TenantRepository {

    override fun observeCurrentTenant(): Flow<Tenant?> =
        sessionLocalDataSource.tenantId.flatMapLatest { tenantId ->
            if (tenantId.isNullOrBlank()) flowOf(null)
            else tenantDataSource.observeTenant(tenantId).map { it?.toDomain() }
        }

    override suspend fun createTenant(
        name: String,
        ownerUid: String,
        ownerName: String
    ): Resource<Tenant> = try {
        val dto = tenantDataSource.createTenant(name, ownerUid, ownerName)
        sessionLocalDataSource.saveSession(dto.id, Role.OWNER.name) // antes ADMIN: el creador es OWNER
        Resource.Success(dto.toDomain())
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear el negocio", e)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeMyTenants(uid: String): Flow<List<TenantMembership>> =
        tenantDataSource.observeMyMemberships(uid).flatMapLatest { memberships ->
            if (memberships.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    memberships.map { m ->
                        tenantDataSource.observeTenant(m.tenantId).map { dto ->
                            dto?.let {
                                TenantMembership(
                                    tenant = it.toDomain(),
                                    role = runCatching { Role.valueOf(m.role) }.getOrDefault(Role.OPERATOR),
                                    eventCount = it.eventCount.toInt()
                                )
                            }
                        }
                    }
                ) { it.filterNotNull() }
            }
        }
    override suspend fun joinTenant(invitationCode: String, uid: String): Resource<Tenant> = try {
        val dto = tenantDataSource.joinTenant(invitationCode, uid)
        // was "OPERADOR": the DataStore kept caching the legacy Spanish value
        sessionLocalDataSource.saveSession(dto.id, Role.OPERATOR.name)
        Resource.Success(dto.toDomain())
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Código de invitación inválido", e)
    }
}
