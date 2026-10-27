package com.example.arrax.domain.usecase.business.members

import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.Resource
import com.example.arrax.core.common.toAppError
import com.example.arrax.domain.model.Invitation
import com.example.arrax.domain.repository.InviteRepository
import com.example.arrax.domain.usecase.user.ObserveCurrentUserUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

// domain/usecase/business/members/CreateInvitationUseCase.kt
class CreateInvitationUseCase @Inject constructor(
    private val inviteRepository: InviteRepository,
    private val observeCurrentUser: ObserveCurrentUserUseCase
) {
    suspend operator fun invoke(tenantId: String): Result<Invitation> {
        val user = observeCurrentUser().first() ?: return Result.failure(AppError.UserNotFound())
        return when (val r = inviteRepository.createInvite(tenantId, user.uid)) {
            is Resource.Success -> Result.success(r.data)
            is Resource.Error -> Result.failure(r.throwable?.toAppError() ?: AppError.Unknown())
            Resource.Loading -> Result.failure(AppError.Unknown())
        }
    }
}