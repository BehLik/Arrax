package com.example.arrax.domain.usecase.auth.verification

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.repository.AuthRepository
import javax.inject.Inject

class SendEmailVerificationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Resource<Unit> = authRepository.sendEmailVerification()
}