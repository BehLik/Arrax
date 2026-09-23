package com.example.arrax.data.repository


import com.example.arrax.core.common.Resource
import com.example.arrax.data.remote.FirebaseAuthDataSource
import com.example.arrax.data.remote.FirestoreTenantDataSource
import com.example.arrax.domain.model.Role
import com.example.arrax.domain.model.User
import com.example.arrax.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: FirebaseAuthDataSource,
    private val tenantDataSource: FirestoreTenantDataSource
) : AuthRepository {

    override fun observeCurrentUser(): Flow<User?> =
        authDataSource.observeAuthState().flatMapLatest { firebaseUser ->
            if (firebaseUser == null) {
                flowOf(null)
            } else {
                tenantDataSource.observeUser(firebaseUser.uid).map { dto ->
                    dto?.let {
                        User(
                            uid = it.uid,
                            name = it.name,
                            tenantId = it.tenantId,
                            role = runCatching { Role.valueOf(it.role) }.getOrDefault(Role.OPERADOR)
                        )
                    } ?: User(uid = firebaseUser.uid)
                }
            }
        }

    override suspend fun login(email: String, password: String): Resource<User> = try {
        val firebaseUser = authDataSource.login(email, password)
        Resource.Success(User(uid = firebaseUser.uid, name = firebaseUser.displayName.orEmpty()))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al iniciar sesión", e)
    }

    override suspend fun register(email: String, password: String, name: String): Resource<User> = try {
        val firebaseUser = authDataSource.register(email, password)
        Resource.Success(User(uid = firebaseUser.uid, name = name))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al registrar", e)
    }

    override suspend fun sendPasswordReset(email: String): Resource<Unit> = try {
        authDataSource.sendPasswordReset(email)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al enviar correo de recuperación", e)
    }

    override suspend fun logout() = authDataSource.logout()
}
