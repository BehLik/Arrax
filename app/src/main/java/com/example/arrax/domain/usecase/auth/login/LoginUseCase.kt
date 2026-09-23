package com.example.arrax.domain.usecase.auth.login

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.User
import com.example.arrax.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<User> {
        if (email.isBlank() || password.isBlank()) {
            return Resource.Error("Correo y contraseña son obligatorios")
        }
        return authRepository.login(email.trim(), password)
    }
}
