package com.example.arrax.ui.screen.business.members

data class MemberUi(
    val id: String,
    val name: String,
    val initials: String,
    val roleLabel: String,
    val isOwner: Boolean = false,
    val photoUrl: String? = null
)

data class InvitationItemUi(
    val code: String,
    val typeLabel: String,
    val roleLabel: String,
    val details: String,
    val isActive: Boolean
)

data class BusinessMembersState(
    val members: List<MemberUi> = emptyList(),
    val isLoadingMembers: Boolean = true,
    val membersError: String? = null,
    val activeInvitations: List<InvitationItemUi> = emptyList(),
    val expiredInvitations: List<InvitationItemUi> = emptyList(),
    val activeExpanded: Boolean = true,
    val expiredExpanded: Boolean = false,
)