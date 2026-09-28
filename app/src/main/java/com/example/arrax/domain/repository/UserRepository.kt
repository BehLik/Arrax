package com.example.arrax.domain.repository

import com.example.arrax.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeCurrentUser(): Flow<User?>
}
