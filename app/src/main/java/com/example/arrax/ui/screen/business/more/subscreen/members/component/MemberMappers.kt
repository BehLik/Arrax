package com.example.arrax.ui.screen.business.more.subscreen.members.component

import androidx.compose.ui.graphics.ImageBitmap
import com.example.arrax.core.common.toLabel
import com.example.arrax.domain.model.InviteStatus
import com.example.arrax.domain.model.Member
import com.example.arrax.domain.model.Role
import com.example.arrax.ui.screen.business.more.subscreen.members.MemberUi

enum class InviteConfirm { Revoke, Delete }

data class InvitationDetailUi(
    val code: String,
    val typeLabel: String,
    val roleLabel: String,
    val usesLabel: String,
    val expiresLabel: String,
    val status: InviteStatus,
    val statusLabel: String,
    val showCode: Boolean,
    val showQr: Boolean,
    val shareMessage: String
)

data class InvitationDetailState(
    val isLoading: Boolean = true,
    val invitation: InvitationDetailUi? = null,
    val guests: List<MemberUi> = emptyList(),
    val qrImage: ImageBitmap? = null,
    val confirm: InviteConfirm? = null,
    val isWorking: Boolean = false,
    val error: String? = null,
)
fun Member.toMemberUi() = MemberUi(
    id = uid,
    name = name,
    initials = name.split(" ").filter { it.isNotBlank() }.take(2)
        .joinToString("") { it.first().uppercase() }.ifEmpty { "?" },
    role = role,
    roleLabel = role.toLabel(),
    joinedLabel = ""
)
sealed interface InvitationDetailEvent { data object Deleted : InvitationDetailEvent }