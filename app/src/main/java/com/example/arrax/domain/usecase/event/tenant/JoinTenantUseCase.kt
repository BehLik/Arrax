package com.example.arrax.domain.usecase.event.tenant


import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.Tenant
import com.example.arrax.domain.repository.TenantRepository
import javax.inject.Inject

class JoinTenantUseCase @Inject constructor(
    private val tenantRepository: TenantRepository
) {
    suspend operator fun invoke(invitationCode: String, uid: String): Resource<Tenant> {
        if (invitationCode.isBlank()) return Resource.Error("El código de invitación es obligatorio")
        return tenantRepository.joinTenant(invitationCode.trim(), uid)
    }
}
