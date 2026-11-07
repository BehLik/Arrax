package com.example.arrax.domain.usecase.tenant.observe

import com.example.arrax.domain.model.Role
import com.example.arrax.domain.repository.TenantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ObserveMyRoleUseCase @Inject constructor(
    private val tenantRepository: TenantRepository
) {
    operator fun invoke(tenantId: String, uid: String): Flow<Role> =
        tenantRepository.observeMyRole(tenantId, uid)
}