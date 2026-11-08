package com.example.arrax.domain.usecase.access

import com.example.arrax.domain.model.Access
import com.example.arrax.domain.model.Role
import com.example.arrax.domain.repository.AuthRepository
import com.example.arrax.domain.repository.TenantRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


class ObserveAccessUseCase @Inject constructor(
    private val tenantRepository: TenantRepository,
    private val authRepository: AuthRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(tenantId: String): Flow<Access> =
        authRepository.observeCurrentUser()
            .map { it?.uid }
            .distinctUntilChanged()
            .flatMapLatest { uid ->
                if (uid == null) flowOf(Access.None)
                else tenantRepository.observeMyRole(tenantId, uid)
                    .map { role -> if (role == Role.NONE) Access.None else Access.Granted(role) }
            }
            .onStart { emit(Access.Loading) }
            .catch { emit(Access.None) }
}