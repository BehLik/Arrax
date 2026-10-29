package com.example.arrax.domain.usecase.tenant.observe

import com.example.arrax.domain.model.Tenant
import com.example.arrax.domain.repository.TenantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTenantUseCase @Inject constructor(
    private val tenantRepository: TenantRepository
) {
    operator fun invoke(tenantId: String): Flow<Tenant?> =
        tenantRepository.observeTenant(tenantId)
}