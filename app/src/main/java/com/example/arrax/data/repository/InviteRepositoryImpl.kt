package com.example.arrax.data.repository

import com.example.arrax.core.common.Resource
import com.example.arrax.data.remote.FirestoreInviteDataSource
import com.example.arrax.domain.model.Invitation
import com.example.arrax.domain.model.Role
import com.example.arrax.domain.repository.InviteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InviteRepositoryImpl @Inject constructor(
    private val dataSource: FirestoreInviteDataSource
) : InviteRepository {
    override suspend fun createInvite(tenantId: String, createdBy: String): Resource<Invitation> = try {
        val dto = dataSource.createInvite(tenantId, createdBy, TTL_MILLIS)
        Resource.Success(
            Invitation(dto.code, dto.tenantId, dto.tenantName, Role.OPERATOR, dto.expiresAt)
        )
    } catch (e: Exception) {
        Resource.Error(e.message ?: "No se pudo crear la invitación", e)
    }

    private companion object { const val TTL_MILLIS = 72L * 60 * 60 * 1000 } // 72 h
}