package com.example.arrax.domain.usecase.business.members

import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.Resource
import com.example.arrax.core.common.toAppError
import com.example.arrax.domain.model.Invitation
import com.example.arrax.domain.model.InviteType
import com.example.arrax.domain.model.Role
import com.example.arrax.domain.repository.InviteRepository
import com.example.arrax.domain.usecase.user.ObserveCurrentUserUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CreateInvitationUseCase @Inject constructor(
    private val inviteRepository: InviteRepository,
    private val observeCurrentUser: ObserveCurrentUserUseCase
) {
    suspend operator fun invoke(
        tenantId: String,
        role: Role,
        type: InviteType,
        maxUses: Int
    ): Result<Invitation> {
        if (maxUses !in 1..10) return Result.failure(AppError.Validation("maxUses"))
        val user = observeCurrentUser().first() ?: return Result.failure(AppError.UserNotFound())
        return when (val r = inviteRepository.createInvite(tenantId, user.uid, role, type, maxUses)) {
            is Resource.Success -> Result.success(r.data)
            is Resource.Error -> Result.failure(r.throwable?.toAppError() ?: AppError.Unknown())
            Resource.Loading -> Result.failure(AppError.Unknown())
        }
    }
}