package com.example.arrax.ui.screen.business.members.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.display.ArxAvatar
import com.example.arrax.core.designsystem.components.display.ArxListItem
import com.example.arrax.core.designsystem.components.feedback.ArxChipStatus
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.ui.screen.business.members.MemberUi

@Composable
internal fun MemberRow(
    member: MemberUi,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ArxListItem(
        title = member.name,
        modifier = modifier,
        showChevron = false,
        leading = { ArxAvatar(initials = member.initials) },
        trailing = {
            if (member.isOwner) {
                ArxStatusChip(
                    text = member.roleLabel,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            } else {
                ArxStatusChip(text = member.roleLabel, status = ArxChipStatus.Info)
            }
        },
        onClick = { onClick(member.id) }
    )
}