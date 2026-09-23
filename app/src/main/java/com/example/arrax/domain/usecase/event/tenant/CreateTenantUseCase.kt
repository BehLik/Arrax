package com.example.arrax.domain.usecase.event.tenant

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.Tenant
import com.example.arrax.domain.repository.TenantRepository
import javax.inject.Inject

class CreateTenantUseCase @Inject constructor(
    private val tenantRepository: TenantRepository
) {
    suspend operator fun invoke(name: String, adminUid: String): Resource<Tenant> {
        if (name.isBlank()) return Resource.Error("El nombre del negocio es obligatorio")
        return tenantRepository.createTenant(name.trim(), adminUid)
    }
}