package com.example.arrax.ui.screen.business.members.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.display.ArxAvatar
import com.example.arrax.ui.screen.business.BusinessListRow
import com.example.arrax.ui.screen.business.BusinessPill
import com.example.arrax.ui.screen.business.BusinessTone
import com.example.arrax.ui.screen.business.members.MemberUi

@Composable
internal fun MemberRow(
    member: MemberUi,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BusinessListRow(
        title = member.name,
        modifier = modifier,
        showChevron = false,
        leading = { ArxAvatar(initials = member.initials) },
        trailing = {
            BusinessPill(
                text = member.roleLabel,
                tone = if (member.isOwner) BusinessTone.Primary else BusinessTone.Info
            )
        },
        onClick = { onClick(member.id) }
    )
}