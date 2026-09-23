package com.example.arrax.domain.repository


import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.Tenant
import kotlinx.coroutines.flow.Flow

interface TenantRepository {
    fun observeCurrentTenant(): Flow<Tenant?>
    suspend fun createTenant(name: String, adminUid: String): Resource<Tenant>
    suspend fun joinTenant(invitationCode: String, uid: String): Resource<Tenant>
}
