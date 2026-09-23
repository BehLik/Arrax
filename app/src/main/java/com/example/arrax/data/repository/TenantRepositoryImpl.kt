package com.example.arrax.data.repository

import com.example.arrax.core.common.Resource
import com.example.arrax.data.local.SesionLocalDataSource
import com.example.arrax.data.mapper.toDomain
import com.example.arrax.data.remote.FirestoreTenantDataSource
import com.example.arrax.domain.model.Tenant
import com.example.arrax.domain.repository.TenantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TenantRepositoryImpl @Inject constructor(
    private val tenantDataSource: FirestoreTenantDataSource,
    private val sessionLocalDataSource: SesionLocalDataSource
) : TenantRepository {

    override fun observeCurrentTenant(): Flow<Tenant?> =
        sessionLocalDataSource.tenantId.flatMapLatest { tenantId ->
            if (tenantId.isNullOrBlank()) flowOf(null)
            else tenantDataSource.observeTenant(tenantId).map { it?.toDomain() }
        }

    override suspend fun createTenant(name: String, adminUid: String): Resource<Tenant> = try {
        val dto = tenantDataSource.createTenant(name, adminUid)
        sessionLocalDataSource.saveSession(dto.id, "ADMIN")
        Resource.Success(dto.toDomain())
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear el negocio", e)
    }

    override suspend fun joinTenant(invitationCode: String, uid: String): Resource<Tenant> = try {
        val dto = tenantDataSource.joinTenant(invitationCode, uid)
        sessionLocalDataSource.saveSession(dto.id, "OPERADOR")
        Resource.Success(dto.toDomain())
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Código de invitación inválido", e)
    }
}
