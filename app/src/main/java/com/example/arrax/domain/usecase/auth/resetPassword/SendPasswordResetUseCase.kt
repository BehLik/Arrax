package com.example.arrax.domain.usecase.auth.resetPassword


import com.example.arrax.core.common.Resource
import com.example.arrax.domain.repository.AuthRepository
import javax.inject.Inject

class SendPasswordResetUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Resource<Unit> {
        if (email.isBlank()) return Resource.Error("El correo es obligatorio")
        return authRepository.sendPasswordReset(email.trim())
    }
}
