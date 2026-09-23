package com.example.arrax.domain.usecase.user

import com.example.arrax.domain.model.User
import com.example.arrax.domain.repository.AuthRepository

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCurrentUserUseCase @Inject constructor(
    private val userRepository: AuthRepository
) {
    operator fun invoke(): Flow<User?> = userRepository.observeCurrentUser()
}