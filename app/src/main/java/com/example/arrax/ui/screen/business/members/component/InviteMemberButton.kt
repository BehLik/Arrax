package com.example.arrax.ui.screen.business.members.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing

@Composable
internal fun InviteMemberButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArxPrimaryButton(
        text = "Crear invitación",
        onClick = onClick,
        modifier = modifier.padding(horizontal = ArxSpacing.md),
        leadingIcon = { ArxIcon(icon = ArxIcons.GroupAdd, size = 20.dp) }
    )
}