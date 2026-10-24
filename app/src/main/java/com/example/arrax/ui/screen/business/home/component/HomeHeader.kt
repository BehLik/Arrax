package com.example.arrax.ui.screen.business.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIconBadge
import com.example.arrax.core.designsystem.icons.ArxIconShape
import com.example.arrax.core.designsystem.icons.ArxIconStyle
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing

@Composable
internal fun HomeHeader(
    unread: Int,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(top = ArxSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Arrax", style = MaterialTheme.typography.headlineLarge)
            Text(
                text = "Negocios. Personas. Resultados.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onNotificationsClick) {
            BadgedBox(badge = { if (unread > 0) Badge { Text(unread.toString()) } }) {
                ArxIcon(
                    icon = if (unread > 0) ArxIcons.NotificationFilled else ArxIcons.Notification,
                    contentDescription = "Notificaciones"
                )
            }
        }
        // TODO: reemplazar por ArxAvatar(imageUrl = ...) cuando el state traiga la foto de perfil.
        ArxIconBadge(
            icon = ArxIcons.User,
            style = ArxIconStyle.Soft,
            shape = ArxIconShape.Circle,
            contentDescription = "Perfil"
        )
    }
}