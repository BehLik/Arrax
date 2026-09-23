package com.example.arrax.domain.repository

import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeCurrentUser(): Flow<User?>
    suspend fun login(email: String, password: String): Resource<User>
    suspend fun register(email: String, password: String, name: String): Resource<User>
    suspend fun sendPasswordReset(email: String): Resource<Unit>
    suspend fun logout()
}
