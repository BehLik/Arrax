package com.example.arrax.ui.screen.business.more.subscreen.members.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.display.ArxAvatar
import com.example.arrax.core.designsystem.components.display.ArxListItem
import com.example.arrax.core.designsystem.components.feedback.ArxChipStatus
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.icons.ArxIcons.Box
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.root.session.CurrentUserAvatar
import com.example.arrax.ui.screen.business.more.subscreen.members.MemberUi

@Composable
internal fun MemberRow(member: MemberUi, onClick: (String) -> Unit, modifier: Modifier = Modifier) {
    val family = member.role.chipFamily()
    ArxListItem(
        title = if (member.isCurrentUser) "${member.name} (Tú)" else member.name,
        subtitle = member.joinedLabel,
        modifier = modifier,
        showChevron = member.canEdit,
        leading = {
            Box(Modifier.border(2.dp, family.color, CircleShape).padding(ArxSpacing.xxs)) {
                if (member.isCurrentUser) CurrentUserAvatar(size = 36.dp)
                else ArxAvatar(initials = member.initials, imageUrl = member.photoUrl, size = 36.dp)
            }
        },
        trailing = {
            ArxStatusChip(
                text = member.roleLabel,
                containerColor = family.colorContainer,
                contentColor = family.onColorContainer
            )
        },
        onClick = { if (member.canEdit) onClick(member.id) }
    )
}