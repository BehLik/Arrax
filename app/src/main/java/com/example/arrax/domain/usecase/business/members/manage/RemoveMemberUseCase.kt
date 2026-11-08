package com.example.arrax.domain.usecase.business.members.manage

import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.Resource
import com.example.arrax.core.common.toAppError
import com.example.arrax.domain.repository.TenantRepository
import javax.inject.Inject

class RemoveMemberUseCase @Inject constructor(private val repo: TenantRepository) {
    suspend operator fun invoke(tenantId: String, memberUid: String): Result<Unit> =
        repo.removeMember(tenantId, memberUid).toResult()
}