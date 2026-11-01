package com.example.arrax.ui.screen.business.more.subscreen.members.subscreen.invitation

import androidx.compose.ui.graphics.ImageBitmap
import com.example.arrax.domain.model.InviteType
import com.example.arrax.domain.model.Role
//file MembersInvitationState.kt here
enum class InviteStep { Form, Done }

data class InvitationCreateState(
    val step: InviteStep = InviteStep.Form,
    val type: InviteType = InviteType.CODE_QR,
    val role: Role = Role.OPERATOR,
    val people: Int = 1,
    val isCreating: Boolean = false,
    val error: String? = null,
    val result: CreatedInvitationUi? = null,
)

data class CreatedInvitationUi(
    val type: InviteType,
    val typeLabel: String,
    val roleLabel: String,
    val peopleLabel: String,
    val expiresLabel: String,
    val code: String,
    val shareMessage: String,
    val qrImage: ImageBitmap?
)