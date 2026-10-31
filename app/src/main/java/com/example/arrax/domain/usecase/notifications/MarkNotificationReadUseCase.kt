package com.example.arrax.domain.usecase.notifications

import com.example.arrax.domain.repository.NotificationRepository
import com.example.arrax.domain.usecase.user.ObserveCurrentUserUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first

class MarkNotificationReadUseCase @Inject constructor(
    private val repo: NotificationRepository,
    private val observeCurrentUser: ObserveCurrentUserUseCase
) {
    suspend operator fun invoke(tenantId: String, id: String) {
        val user = observeCurrentUser().first() ?: return
        repo.markRead(tenantId, id, user.uid)
    }
}