package com.example.arrax.data.repository

import com.example.arrax.core.common.Resource
import com.example.arrax.data.mapper.toUser
import com.example.arrax.data.remote.FirestoreUserDataSource
import com.example.arrax.domain.model.User
import com.example.arrax.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDataSource: FirestoreUserDataSource
) : UserRepository {

    override fun observeCurrentUser(): Flow<User?> {
        return userDataSource.observeCurrentUserDoc().map { it?.toUser() }
    }
    override fun updateName(name: String): Resource<Unit> = try {
        userDataSource.updateName(name)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al guardar el nombre", e)
    }
}