package com.example.arrax.ui.screen.lobby.home.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.display.ArxEmptyState
import com.example.arrax.core.designsystem.icons.ArxIcons

@Composable
fun LobbyEmptyNegocios(onCrearNegocioClick: () -> Unit, modifier: Modifier = Modifier) {
    ArxEmptyState(
        title = "Aún no tienes negocios",
        description = "Crea uno nuevo o únete con un código de invitación.",
        iconRes = ArxIcons.Box,
        actionText = "Crear negocio",
        onActionClick = onCrearNegocioClick,
        modifier = modifier
    )
}