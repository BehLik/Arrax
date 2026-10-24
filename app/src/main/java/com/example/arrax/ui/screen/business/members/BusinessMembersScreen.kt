package com.example.arrax.ui.screen.business.members

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.ui.screen.business.BusinessScreenSurface
import com.example.arrax.ui.screen.business.BusinessTopBar
import com.example.arrax.ui.screen.business.members.component.InvitationSheet
import com.example.arrax.ui.screen.business.members.component.InviteMemberButton
import com.example.arrax.ui.screen.business.members.component.MembersList

@Composable
fun BusinessMembersScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onMemberClick: (memberId: String) -> Unit = {},
    onShareQr: () -> Unit = {},
    onShareLink: () -> Unit = {},
    viewModel: BusinessMembersViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val clipboard = LocalClipboardManager.current

    BusinessMembersContent(
        state = state,
        onBack = onBack,
        onMemberClick = onMemberClick,
        onInviteClick = viewModel::onInviteClick,
        onDismissInvite = viewModel::onDismissInvite,
        onInviteTabSelected = viewModel::onInviteTabSelected,
        onCopyCode = { state.invitation?.code?.let { clipboard.setText(AnnotatedString(it)) } },
        onShareQr = onShareQr,
        onShareLink = onShareLink,
        onGenerateInvitation = viewModel::onGenerateInvitation,
        modifier = modifier
    )
}

@Composable
internal fun BusinessMembersContent(
    state: BusinessMembersState,
    onBack: () -> Unit,
    onMemberClick: (String) -> Unit,
    onInviteClick: () -> Unit,
    onDismissInvite: () -> Unit,
    onInviteTabSelected: (InviteTab) -> Unit,
    onCopyCode: () -> Unit,
    onShareQr: () -> Unit,
    onShareLink: () -> Unit,
    onGenerateInvitation: () -> Unit,
    modifier: Modifier = Modifier
) {
    BusinessScreenSurface(modifier) {
        ArxBackground(decoration = ArxDecoration.None) {
            Column(modifier = Modifier.fillMaxSize()) {
                BusinessTopBar(title = "Equipo y permisos", onBack = onBack)
                InviteMemberButton(onClick = onInviteClick)
                MembersList(
                    members = state.members,
                    onMemberClick = onMemberClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (state.isInviteSheetVisible) {
            InvitationSheet(
                state = state,
                onDismiss = onDismissInvite,
                onTabSelected = onInviteTabSelected,
                onCopyCode = onCopyCode,
                onShareQr = onShareQr,
                onShareLink = onShareLink,
                onGenerate = onGenerateInvitation
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Preview(
    showBackground = true, widthDp = 360, heightDp = 780,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun BusinessMembersPreview() {
    ArxTheme(darkTheme = isSystemInDarkTheme()) {
        BusinessMembersContent(
            state = BusinessMembersState(
                members = listOf(
                    MemberUi("1", "Alex Caamal", "AC", "Owner", isOwner = true),
                    MemberUi("2", "Carlos Ramírez", "CR", "Operador"),
                    MemberUi("3", "María López", "ML", "Admin")
                )
            ),
            onBack = {}, onMemberClick = {}, onInviteClick = {}, onDismissInvite = {},
            onInviteTabSelected = {}, onCopyCode = {}, onShareQr = {}, onShareLink = {},
            onGenerateInvitation = {}
        )
    }
}