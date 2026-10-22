package com.example.arrax.ui.screen.lobby.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.ui.screen.lobby.notification.component.NotificationEmptyContent
import com.example.arrax.ui.screen.lobby.notification.component.NotificationItem
import com.example.arrax.ui.screen.lobby.notification.component.NotificationUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyNotificationScreen(
    notifications: List<NotificationUi>,
    onBack: () -> Unit,
    onNotificationClick: (NotificationUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notificaciones",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        if (notifications.isEmpty()) {
            NotificationEmptyContent(modifier = Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(
                    horizontal = ArxSpacing.md,
                    vertical = ArxSpacing.sm
                ),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
            ) {
                items(notifications, key = { it.id }) { item ->
                    NotificationItem(
                        notification = item,
                        onClick = { onNotificationClick(item) }
                    )
                }
            }
        }
    }
}

private val previewNotifications = listOf(
    NotificationUi("1", "Nuevo pedido #045", "Juan Pérez ha registrado un nuevo pedido de Lomo.", "Hace 10 min", false),
    NotificationUi("2", "Lote #001 Cerrado", "Se ha generado el reporte de cierre del lote.", "Hace 2 horas", true),
    NotificationUi("3", "Sincronización exitosa", "Tus datos se han respaldado en la nube.", "Ayer", true)
)

@Preview(name = "Light", widthDp = 360, heightDp = 720)
@Composable
private fun NotificationLightPreview() {
    ArxTheme(darkTheme = false) {
        LobbyNotificationScreen(previewNotifications, onBack = {}, onNotificationClick = {})
    }
}

@Preview(name = "Dark", widthDp = 360, heightDp = 720)
@Composable
private fun NotificationDarkPreview() {
    ArxTheme(darkTheme = true) {
        LobbyNotificationScreen(previewNotifications, onBack = {}, onNotificationClick = {})
    }
}

@Preview(name = "Empty", widthDp = 360, heightDp = 720)
@Composable
private fun NotificationEmptyPreview() {
    ArxTheme(darkTheme = true) {
        LobbyNotificationScreen(emptyList(), onBack = {}, onNotificationClick = {})
    }
}