package com.example.arrax.domain.usecase.business.create

import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.Resource
import com.example.arrax.core.common.toAppError
import com.example.arrax.domain.model.Tenant
import com.example.arrax.domain.repository.TenantRepository
import com.example.arrax.domain.usecase.user.ObserveCurrentUserUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CreateBusinessUseCase @Inject constructor(
    private val tenantRepository: TenantRepository,
    private val observeCurrentUser: ObserveCurrentUserUseCase
) {
    suspend operator fun invoke(rawName: String): Result<Tenant> {
        val name = rawName.trim()
        if (name.isBlank() || name.length > MAX_NAME) {
            return Result.failure(AppError.Validation("name"))
        }
        val user = observeCurrentUser().first()
            ?: return Result.failure(AppError.UserNotFound())

        return when (val result = tenantRepository.createTenant(name, user.uid, user.name)) {
            is Resource.Success -> Result.success(result.data)
            is Resource.Error -> Result.failure(
                result.throwable?.toAppError() ?: AppError.Unknown()
            )
            Resource.Loading -> Result.failure(AppError.Unknown())
        }
    }

    companion object { const val MAX_NAME = 60 }
}