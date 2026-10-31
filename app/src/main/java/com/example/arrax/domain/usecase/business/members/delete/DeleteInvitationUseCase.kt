package com.example.arrax.domain.usecase.business.members.delete

import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.Resource
import com.example.arrax.core.common.toAppError
import com.example.arrax.domain.repository.InviteRepository
import javax.inject.Inject


    class DeleteInvitationUseCase @Inject constructor(private val repo: InviteRepository) {
        suspend operator fun invoke(code: String): Result<Unit> = repo.deleteInvite(code).toResult()
    }

private fun Resource<Unit>.toResult(): Result<Unit> = when (this) {
    is Resource.Success -> Result.success(Unit)
    is Resource.Error -> Result.failure(throwable?.toAppError() ?: AppError.Unknown())
    Resource.Loading -> Result.failure(AppError.Unknown())
}