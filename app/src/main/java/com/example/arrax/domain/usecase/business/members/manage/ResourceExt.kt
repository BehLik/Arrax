package com.example.arrax.domain.usecase.business.members.manage

import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.Resource
import com.example.arrax.core.common.toAppError

internal fun Resource<Unit>.toResult(): Result<Unit> = when (this) {
    is Resource.Success -> Result.success(Unit)
    is Resource.Error -> Result.failure(throwable?.toAppError() ?: AppError.Unknown())
    Resource.Loading -> Result.failure(AppError.Unknown())
}