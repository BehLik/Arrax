package com.example.arrax.domain.usecase.business.members

import com.example.arrax.domain.model.Member
import com.example.arrax.domain.repository.TenantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMembersUseCase @Inject constructor(
    private val tenantRepository: TenantRepository
) {
    operator fun invoke(tenantId: String): Flow<List<Member>> =
        tenantRepository.observeMembers(tenantId)
}