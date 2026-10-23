package com.example.arrax.domain.usecase.user

import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.Resource
import com.example.arrax.domain.repository.UserRepository
import javax.inject.Inject


class UpdateDisplayNameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(name: String): Resource<Unit> {
        val clean = name.trim().replace(Regex("\\s+"), " ")
        if (clean.isEmpty()) return Resource.Error("Ingresa tu nombre", AppError.Validation("name"))
        if (clean.length > MAX_NAME_LENGTH) {
            return Resource.Error("El nombre es demasiado largo", AppError.Validation("name"))
        }
        return userRepository.updateName(clean)
    }

    companion object { const val MAX_NAME_LENGTH = 60 }
}