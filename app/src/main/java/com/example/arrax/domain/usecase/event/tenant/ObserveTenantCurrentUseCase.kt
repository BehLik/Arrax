package com.example.arrax.domain.usecase.event.tenant

import com.example.arrax.domain.model.Tenant
import com.example.arrax.domain.repository.TenantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTenantCurrentUseCase @Inject constructor(
    private val tenantRepository: TenantRepository
) {
    operator fun invoke(): Flow<Tenant?> = tenantRepository.observeCurrentTenant()
}
