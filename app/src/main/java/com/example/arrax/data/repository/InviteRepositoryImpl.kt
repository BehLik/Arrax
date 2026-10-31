package com.example.arrax.data.repository

import com.example.arrax.core.common.Resource
import com.example.arrax.data.remote.FirestoreInviteDataSource
import com.example.arrax.data.remote.toDomain
import com.example.arrax.domain.model.Invitation
import com.example.arrax.domain.model.InviteType
import com.example.arrax.domain.model.Role
import com.example.arrax.domain.repository.InviteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class InviteRepositoryImpl @Inject constructor(
    private val dataSource: FirestoreInviteDataSource
) : InviteRepository {

    override suspend fun createInvite(
        tenantId: String,
        createdBy: String,
        role: Role,
        type: InviteType,
        maxUses: Int,
        validDays: Int
    ): Resource<Invitation> = try {
        val dto = dataSource.createInvite(tenantId, createdBy, role, type, maxUses, validDays)
        Resource.Success(dto.toDomain())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Resource.Error(e.message ?: "No se pudo crear la invitación", e)
    }

    override fun observeInvites(tenantId: String): Flow<List<Invitation>> =
        dataSource.observeInvites(tenantId)
    override fun observeInvite(code: String) = dataSource.observeInvite(code)

    override suspend fun revokeInvite(code: String): Resource<Unit> = try {
        dataSource.revokeInvite(code); Resource.Success(Unit)
    } catch (e: CancellationException) { throw e
    } catch (e: Exception) { Resource.Error(e.message ?: "No se pudo cancelar la invitación", e) }

    override suspend fun deleteInvite(code: String): Resource<Unit> = try {
        dataSource.deleteInvite(code); Resource.Success(Unit)
    } catch (e: CancellationException) { throw e
    } catch (e: Exception) { Resource.Error(e.message ?: "No se pudo eliminar la invitación", e) }
}