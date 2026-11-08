package com.example.arrax.ui.screen.business.more.subscreen.members

import com.example.arrax.domain.model.Role

data class MemberUi(
    val id: String,
    val name: String,
    val initials: String,
    val role: Role,
    val roleLabel: String,
    val joinedLabel: String,
    val isCurrentUser: Boolean = false,
    val canEdit: Boolean = false,
    val photoUrl: String? = null
) { val isOwner get() = role == Role.OWNER }
data class InvitationItemUi(
    val code: String,
    val typeLabel: String,
    val roleLabel: String,
    val details: String,
    val statusLabel: String,
    val isActive: Boolean
)

data class BusinessMembersState(
    val members: List<MemberUi> = emptyList(),
    val canManage: Boolean = false,
    val isLoadingMembers: Boolean = true,
    val membersError: String? = null,
    val activeInvitations: List<InvitationItemUi> = emptyList(),
    val expiredInvitations: List<InvitationItemUi> = emptyList(),
    val activeExpanded: Boolean = true,
    val expiredExpanded: Boolean = false,
    val myRole: Role? = null,
    val selectedId: String? = null,
    val confirmRemove: Boolean = false,
    val isWorking: Boolean = false,
    val actionError: String? = null,
) {
    val selectedMember: MemberUi? get() = members.firstOrNull { it.id == selectedId }
}