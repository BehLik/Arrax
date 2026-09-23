package com.example.arrax.ui.screen.lobby.home.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.display.ArxEmptyState
import com.example.arrax.core.designsystem.icons.ArxIcons

@Composable
fun LobbyEmptyEventos(esAdmin: Boolean, onCrearEventoClick: () -> Unit, modifier: Modifier = Modifier) {
    ArxEmptyState(
        title = "Aún no hay eventos",
        description = if (esAdmin) "Crea tu primer evento para empezar a vender" else "Cuando el administrador cree un evento aparecerá aquí",
        icon = ArxIcons.Event,
        actionLabel = if (esAdmin) "Crear evento" else null,
        onActionClick = if (esAdmin) onCrearEventoClick else null,
        modifier = modifier.fillMaxSize()
    )
}