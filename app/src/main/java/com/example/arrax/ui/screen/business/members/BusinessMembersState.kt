package com.example.arrax.ui.screen.business.members

import androidx.compose.ui.graphics.ImageBitmap

enum class InviteTab { Code, Qr }

data class MemberUi(
    val id: String,
    val name: String,
    val initials: String,
    val roleLabel: String,
    val isOwner: Boolean = false
)

//data class InvitationUi(
//    val code: String,
//    val expiresLabel: String,
//    val usesLabel: String,
//    val roleLabel: String,
//    val qrImage: ImageBitmap? = null
//)
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
    val invitation: InvitationUi? = null,
    val isGenerating: Boolean = false
)