package com.example.arrax.ui.screen.lobby.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.buttons.ArxExtendedFAB
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.ui.screen.lobby.home.component.EventoListItem
import com.example.arrax.ui.screen.lobby.home.component.LobbyEmptyEventos

@Composable
fun LobbyHomeScreen(
    onEventoClick: (String) -> Unit,
    onNuevoEventoClick: () -> Unit,
    viewModel: LobbyHomeViewmodel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    ArxBackground(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { ArxTopBar(title = "Mis eventos") },
            floatingActionButton = {
                if (state.esAdmin && !state.isLoading) {
                    ArxExtendedFAB(text = "Nuevo evento", icon = ArxIcons.Add, onClick = onNuevoEventoClick)
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                when {
                    state.isLoading -> ArxLoading(message = "Cargando eventos...")
                    state.eventos.isEmpty() -> LobbyEmptyEventos(
                        esAdmin = state.esAdmin,
                        onCrearEventoClick = onNuevoEventoClick,
                        modifier = Modifier.fillMaxSize()
                    )
                    else -> Column(modifier = Modifier.fillMaxSize()) {
                        if (state.estaSinConexion) SinConexionBadge()
                        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
                            items(state.eventos, key = { it.id }) { evento ->
                                EventoListItem(
                                    evento = evento,
                                    onClick = { onEventoClick(evento.id) },
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SinConexionBadge() {
    Surface(color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Sin conexión — mostrando datos guardados",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}