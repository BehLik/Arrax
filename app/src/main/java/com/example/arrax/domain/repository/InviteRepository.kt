package com.example.arrax.domain.repository

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.Invitation
import com.example.arrax.domain.model.InviteType
import com.example.arrax.domain.model.Role
import kotlinx.coroutines.flow.Flow

interface InviteRepository {
    suspend fun createInvite(
        tenantId: String,
        createdBy: String,
        role: Role,
        type: InviteType,
        maxUses: Int,
        validDays: Int = 7
    ): Resource<Invitation>
    fun observeInvites(tenantId: String): Flow<List<Invitation>>
}