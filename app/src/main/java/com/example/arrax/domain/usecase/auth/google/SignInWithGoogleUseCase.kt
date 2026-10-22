package com.example.arrax.domain.usecase.auth.google

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.User
import com.example.arrax.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Resource<User> {
        if (idToken.isBlank()) return Resource.Error("No se pudo obtener la cuenta de Google")
        return authRepository.signInWithGoogle(idToken)
    }
}