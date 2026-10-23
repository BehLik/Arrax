package com.example.arrax.domain.usecase.auth.changePassword

import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.Resource
import com.example.arrax.domain.repository.AuthRepository
import javax.inject.Inject


class ChangePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(currentPassword: String, newPassword: String): Resource<Unit> {
        if (currentPassword.isBlank() || newPassword.isBlank()) {
            return Resource.Error("Completa todos los campos", AppError.Validation("password"))
        }
        return authRepository.changePassword(currentPassword, newPassword)
    }
}