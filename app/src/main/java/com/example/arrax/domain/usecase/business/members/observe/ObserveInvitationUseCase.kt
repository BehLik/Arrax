package com.example.arrax.domain.usecase.business.members.observe

import com.example.arrax.domain.model.Invitation
import com.example.arrax.domain.repository.InviteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveInvitationUseCase @Inject constructor(
    private val repo: InviteRepository) {
    operator fun invoke(code: String): Flow<Invitation?> = repo.observeInvite(code)
}