package com.example.arrax.domain.usecase.business.members

import com.example.arrax.domain.model.Invitation
import com.example.arrax.domain.repository.InviteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveInvitationsUseCase @Inject constructor(
    private val inviteRepository: InviteRepository
) {
    operator fun invoke(tenantId: String): Flow<List<Invitation>> =
        inviteRepository.observeInvites(tenantId)
}