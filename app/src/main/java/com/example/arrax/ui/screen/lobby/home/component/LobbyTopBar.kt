package com.example.arrax.ui.screen.lobby.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.arrax.R
import com.example.arrax.core.designsystem.components.brand.ArxLogoMark
import com.example.arrax.core.designsystem.components.feedback.ArxBadge
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.root.session.CurrentUserAvatar

@Composable
fun LobbyTopBar(
    unreadNotifications: Int,
    onNotificationsClick: () -> Unit,
//    onSettingsClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArxLogoMark(size = 40.dp, withContainer = false)
        Spacer(Modifier.weight(1f))

        Box {
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    painterResource(ArxIcons.Notification),
                    contentDescription = stringResource(R.string.lobby_notifications_cd)
                )
            }
            if (unreadNotifications > 0) {
                ArxBadge(
                    count = unreadNotifications,
                    maxCount = 99,
                    modifier = Modifier.align(Alignment.TopEnd).padding(top = 4.dp, end = 4.dp)
                )
            }
        }

//        IconButton(onClick = onSettingsClick) {
//            Icon(
//                painterResource(ArxIcons.Settings),
//                contentDescription = stringResource(R.string.lobby_settings_cd)
//            )
//        }

        Box(
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .clip(CircleShape)
                .clickable(
                    onClickLabel = stringResource(R.string.lobby_profile_cd),
                    onClick = onProfileClick
                ),
            contentAlignment = Alignment.Center
        ) {
            CurrentUserAvatar(size = 36.dp)
        }
    }
}