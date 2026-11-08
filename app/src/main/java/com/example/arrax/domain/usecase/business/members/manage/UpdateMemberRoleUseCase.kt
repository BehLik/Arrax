package com.example.arrax.domain.usecase.business.members.manage

import com.example.arrax.core.common.AppError
import com.example.arrax.domain.model.Role
import com.example.arrax.domain.repository.TenantRepository
import javax.inject.Inject

class UpdateMemberRoleUseCase @Inject constructor(private val repo: TenantRepository) {
    suspend operator fun invoke(tenantId: String, memberUid: String, role: Role): Result<Unit> {
        if (role != Role.ADMIN && role != Role.OPERATOR) return Result.failure(AppError.Validation("role"))
        return repo.updateMemberRole(tenantId, memberUid, role).toResult()
    }
}