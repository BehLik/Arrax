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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.brand.ArxLogoMark
import com.example.arrax.core.designsystem.components.display.ArxAvatar
import com.example.arrax.core.designsystem.components.display.arxInitials
import com.example.arrax.core.designsystem.components.feedback.ArxBadge
import com.example.arrax.core.designsystem.icons.ArxIcons

@Composable
fun LobbyTopBar(
    nombreUsuario: String,
    notificacionesSinLeer: Int,
    onNotificacionesClick: () -> Unit,
    onAjustesClick: () -> Unit,
    onPerfilClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArxLogoMark(size = 40.dp, withContainer = false)
        Spacer(Modifier.weight(1f))

        Box {
            IconButton(onClick = onNotificacionesClick) {
                Icon(painterResource(ArxIcons.Notification), contentDescription = "Notificaciones")
            }
            if (notificacionesSinLeer > 0) {
                ArxBadge(
                    count = notificacionesSinLeer,
                    maxCount = 99,
                    modifier = Modifier.align(Alignment.TopEnd).padding(top = 4.dp, end = 4.dp)
                )
            }
        }
        IconButton(onClick = onAjustesClick) {
            Icon(painterResource(ArxIcons.Settings), contentDescription = "Configuración")
        }
        ArxAvatar(
            initials = arxInitials(nombreUsuario.ifBlank { "?" }),
            size = 36.dp,
            modifier = Modifier.clip(CircleShape).clickable(onClick = onPerfilClick)
        )
    }
}