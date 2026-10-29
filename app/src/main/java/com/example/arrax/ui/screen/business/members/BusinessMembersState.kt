package com.example.arrax.ui.screen.business.members

import androidx.compose.ui.graphics.ImageBitmap

enum class InviteTab { Code, Qr }

data class MemberUi(
    val id: String,
    val name: String,
    val initials: String,
    val roleLabel: String,
    val isOwner: Boolean = false,
    val photoUrl: String? = null
)
data class InvitationUi(
    val code: String,            // ya formateado: K7M2-9QPX
    val roleLabel: String,
    val expiresLabel: String,
    val usesLabel: String,
    val shareMessage: String,
    val qrImage: ImageBitmap? = null
)

data class BusinessMembersState(
    val members: List<MemberUi> = emptyList(),
    val isInviteSheetVisible: Boolean = false,
    val inviteTab: InviteTab = InviteTab.Code,
    val isGenerating: Boolean = false,
    val inviteError: String? = null,
    val invitation: InvitationUi? = null,
    val isLoadingMembers: Boolean = true,
    val membersError: String? = null,
)