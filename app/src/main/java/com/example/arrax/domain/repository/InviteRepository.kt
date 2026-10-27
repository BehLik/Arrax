package com.example.arrax.domain.repository

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.Invitation

interface InviteRepository {
    suspend fun createInvite(tenantId: String, createdBy: String): Resource<Invitation>
}