package com.example.arrax.ui.screen.business.more.subscreen.members.subscreen.invitationdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.buttons.ArxFieldActionButton
import com.example.arrax.core.designsystem.components.buttons.ArxTextButton
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.components.display.ArxLabeledValue
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.designsystem.components.feedback.ArxAlertDialog
import com.example.arrax.core.designsystem.components.feedback.ArxChipStatus
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.domain.model.InviteStatus
import com.example.arrax.ui.screen.business.more.subscreen.members.component.InvitationDetailEvent
import com.example.arrax.ui.screen.business.more.subscreen.members.component.InvitationDetailState
import com.example.arrax.ui.screen.business.more.subscreen.members.component.InviteConfirm
import com.example.arrax.ui.screen.business.more.subscreen.members.component.InviteSharing
import com.example.arrax.ui.screen.business.more.subscreen.members.component.MemberRow

@Composable
fun MembersInvitationDetailScreen (
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MembersInvitationDetailViewmodel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { if (it is InvitationDetailEvent.Deleted) onBack() }
    }

    InvitationDetailContent(
        state = state,
        onBack = onBack,
        onCopyCode = { state.invitation?.let { clipboard.setText(AnnotatedString(it.code)) } },
        onShare = {
            val inv = state.invitation ?: return@InvitationDetailContent
            val qr = state.qrImage
            if (inv.showQr && qr != null) InviteSharing.shareQr(context, qr.asAndroidBitmap(), inv.shareMessage)
            else InviteSharing.shareText(context, inv.shareMessage)
        },
        onRevokeClick = viewModel::onRevokeClick,
        onDeleteClick = viewModel::onDeleteClick,
        onConfirm = viewModel::onConfirm,
        onDismissConfirm = viewModel::onDismissConfirm,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InvitationDetailContent(
    state: InvitationDetailState,
    onBack: () -> Unit,
    onCopyCode: () -> Unit,
    onShare: () -> Unit,
    onRevokeClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onConfirm: () -> Unit,
    onDismissConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val inv = state.invitation
    ArxBackground(modifier = modifier, decoration = ArxDecoration.None) {
        Column(Modifier.fillMaxSize()) {
            ArxTopBar(
                title = "Invitación",
                centeredTitle = false,
                navigationType = ArxTopBarNavigationType.BACK,
                onNavigationClick = onBack
            )
            when {
                state.isLoading -> ArxLoading()
                inv == null -> Text(
                    "Esta invitación ya no existe.",
                    modifier = Modifier.padding(ArxSpacing.md),
                    style = MaterialTheme.typography.bodyMedium
                )
                else -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = ArxSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
                ) {
                    ArxCard(modifier = Modifier.fillMaxWidth(), variant = ArxCardVariant.Glass) {
                        Column(Modifier.padding(ArxSpacing.md), verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(inv.typeLabel, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                                ArxStatusChip(
                                    text = inv.statusLabel,
                                    status = if (inv.status == InviteStatus.ACTIVE) ArxChipStatus.Success else ArxChipStatus.Info
                                )
                            }
                            ArxLabeledValue("Rol", inv.roleLabel)
                            ArxLabeledValue("Personas", inv.usesLabel)
                            ArxLabeledValue("Expira", inv.expiresLabel)
                            if (inv.showQr) state.qrImage?.let {
                                Image(
                                    bitmap = it,
                                    contentDescription = "Código QR de la invitación",
                                    modifier = Modifier.size(200.dp).align(Alignment.CenterHorizontally)
                                )
                            }
                            if (inv.showCode) ArxLabeledValue("Código de acceso", inv.code)
                        }
                    }

                    if (inv.status == InviteStatus.ACTIVE) {
                        Row(horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                            if (inv.showCode) ArxFieldActionButton(
                                iconRes = ArxIcons.List, label = "Copiar código",
                                modifier = Modifier.weight(1f), onClick = onCopyCode
                            )
                            ArxFieldActionButton(
                                iconRes = ArxIcons.Link, label = "Compartir",
                                modifier = Modifier.weight(1f), onClick = onShare
                            )
                        }
                    }

                    Text(
                        "Personas invitadas (${state.guests.size})",
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (state.guests.isEmpty()) {
                        Text(
                            "Todavía nadie ha usado esta invitación.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    state.guests.forEach { MemberRow(member = it, onClick = {}) }

                    state.error?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }

                    if (inv.status == InviteStatus.ACTIVE) {
                        ArxTextButton(text = "Cancelar invitación", onClick = onRevokeClick,
                            isDestructive = true, enabled = !state.isWorking)
                    } else {
                        ArxTextButton(text = "Eliminar invitación", onClick = onDeleteClick,
                            isDestructive = true, enabled = !state.isWorking)
                    }
                    Spacer(Modifier.height(ArxSpacing.md))
                }
            }
        }
    }

    state.confirm?.let { action ->
        val revoke = action == InviteConfirm.Revoke
        ArxAlertDialog(
            title = if (revoke) "Cancelar invitación" else "Eliminar invitación",
            message = if (revoke)
                "El código y el QR dejarán de funcionar. Quienes ya se unieron conservan su acceso."
            else
                "Se borrará de la lista. Quienes ya se unieron conservan su acceso.",
            confirmText = if (revoke) "Cancelar invitación" else "Eliminar",
            dismissText = "Volver",
            isDestructive = true,
            onConfirm = onConfirm,
            onDismiss = onDismissConfirm
        )
    }
}