package com.example.arrax.domain.usecase.auth.verification

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.repository.AuthRepository
import javax.inject.Inject

class CheckEmailVerifiedUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /** Success(true) = correo verificado y token refrescado. */
    suspend operator fun invoke(): Resource<Boolean> = authRepository.checkEmailVerified()
}