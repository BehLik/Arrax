package com.example.arrax.domain.usecase.notifications

import com.example.arrax.domain.model.TenantNotification
import com.example.arrax.domain.repository.NotificationRepository
import com.example.arrax.domain.usecase.user.ObserveCurrentUserUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class ObserveNotificationsUseCase @Inject constructor(
    private val repo: NotificationRepository,
    private val observeCurrentUser: ObserveCurrentUserUseCase
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<TenantNotification>> =
        observeCurrentUser().flatMapLatest { user ->
            if (user == null) flowOf(emptyList()) else repo.observeAdminNotifications(user.uid)
        }
}