package com.example.arrax.domain.repository


import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.Member
import com.example.arrax.domain.model.Tenant
import com.example.arrax.domain.model.TenantMembership
import kotlinx.coroutines.flow.Flow

interface TenantRepository {
    fun observeCurrentTenant(): Flow<Tenant?>
    fun observeMyTenants(uid: String): Flow<List<TenantMembership>>
    fun observeTenant(tenantId: String): Flow<Tenant?>
    fun observeMembers(tenantId: String): Flow<List<Member>>
    suspend fun createTenant(name: String, ownerUid: String, ownerName: String): Resource<Tenant>
    suspend fun joinTenant(invitationCode: String, uid: String,userName: String): Resource<Tenant>
}
