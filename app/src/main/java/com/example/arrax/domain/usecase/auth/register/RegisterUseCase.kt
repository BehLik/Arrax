package com.example.arrax.domain.usecase.auth.register

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.User
import com.example.arrax.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, name: String): Resource<User> {
        if (name.isBlank()) return Resource.Error("El nombre es obligatorio")
        if (password.length < 6) return Resource.Error("La contraseña debe tener al menos 6 caracteres")
        return authRepository.register(email.trim(), password, name.trim())
    }
}
