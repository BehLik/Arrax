package com.example.arrax.ui.screen.business.more.subscreen.members.subscreen.invitation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.ui.screen.business.more.subscreen.members.component.InviteSharing
import com.example.arrax.ui.screen.business.more.subscreen.members.subscreen.invitation.component.InvitationDone
import com.example.arrax.ui.screen.business.more.subscreen.members.subscreen.invitation.component.InvitationForm


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembersInvitationScreen(
    onBack: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InvitationCreateViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    ArxBackground(modifier = modifier, decoration = ArxDecoration.None) {
        Column(Modifier.fillMaxSize()) {
            ArxTopBar(
                title = "Crear invitación",
                centeredTitle = false,
                navigationType = ArxTopBarNavigationType.BACK,
                onNavigationClick = onBack
            )
            when (state.step) {
                InviteStep.Form -> InvitationForm(
                    state = state,
                    onTypeSelected = viewModel::onTypeSelected,
                    onRoleSelected = viewModel::onRoleSelected,
                    onPeopleChange = viewModel::onPeopleChange,
                    onNext = viewModel::onNext
                )
                InviteStep.Done -> state.result?.let { r ->
                    InvitationDone(
                        invitation = r,
                        onCopyCode = { clipboard.setText(AnnotatedString(r.code)) },
                        onShare = {
                            val qr = r.qrImage
                            if (qr != null) InviteSharing.shareQr(context, qr.asAndroidBitmap(), r.shareMessage)
                            else InviteSharing.shareText(context, r.shareMessage)
                        },
                        onDone = onDone
                    )
                }
            }
        }
    }
}