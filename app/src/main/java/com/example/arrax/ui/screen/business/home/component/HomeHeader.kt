package com.example.arrax.ui.screen.business.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarVariant
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIconBadge
import com.example.arrax.core.designsystem.icons.ArxIconShape
import com.example.arrax.core.designsystem.icons.ArxIconStyle
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.root.session.CurrentUserAvatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeHeader(
    unread: Int,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArxTopBar(
        title = "Arrax",
        subtitle = "Negocios. Personas. Resultados.",
        variant = ArxTopBarVariant.MENU,
        modifier = modifier,
        actions = {
            IconButton(onClick = onNotificationsClick) {
                BadgedBox(badge = { if (unread > 0) Badge { Text(unread.toString()) } }) {
                    ArxIcon(
                        icon = if (unread > 0) ArxIcons.NotificationFilled else ArxIcons.Notification,
                        contentDescription = "Notificaciones"
                    )
                }
            }
            // TODO: reemplazar por ArxAvatar(imageUrl = ...) cuando el state traiga la foto de perfil.
            CurrentUserAvatar(size = 36.dp)
        }
    )
}