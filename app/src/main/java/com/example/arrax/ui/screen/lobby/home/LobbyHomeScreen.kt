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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.ui.screen.lobby.home.component.LobbyEmptyNegocios
import com.example.arrax.ui.screen.lobby.home.component.LobbyTopBar
import com.example.arrax.ui.screen.lobby.home.component.NegocioListItem
import com.example.arrax.ui.screen.lobby.home.component.RecientesHeader
import com.example.arrax.ui.screen.lobby.home.component.WorkspaceCard

@Composable
fun LobbyHomeScreen(
    onNegocioClick: (String) -> Unit,
    onCrearNegocioClick: () -> Unit,
    onUnirmeClick: () -> Unit,
    onVerTodosClick: () -> Unit,
    onNotificacionesClick: () -> Unit,
    onAjustesClick: () -> Unit,
    onPerfilClick: () -> Unit,
    viewModel: LobbyHomeViewmodel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    ArxBackground(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                LobbyTopBar(
                    nombreUsuario = state.nombreUsuario,
                    notificacionesSinLeer = state.notificacionesSinLeer,
                    onNotificacionesClick = onNotificacionesClick,
                    onAjustesClick = onAjustesClick,
                    onPerfilClick = onPerfilClick
                )
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                if (state.isLoading) {
                    ArxLoading(message = "Cargando negocios...")
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        if (state.estaSinConexion) SinConexionBadge()
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            item {
                                Column(modifier = Modifier.padding(bottom = 20.dp)) {
                                    Text(
                                        text = "Hola, ${state.nombreUsuario}",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        text = "Listo para lo que viene.",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            item {
                                WorkspaceCard(
                                    onCrearNegocioClick = onCrearNegocioClick,
                                    onUnirmeClick = onUnirmeClick,
                                    modifier = Modifier.padding(bottom = 24.dp)
                                )
                            }
                            item {
                                RecientesHeader(
                                    onVerTodosClick = onVerTodosClick,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                            }
                            if (state.negociosRecientes.isEmpty()) {
                                item {
                                    LobbyEmptyNegocios(
                                        onCrearNegocioClick = onCrearNegocioClick,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            } else {
                                items(state.negociosRecientes, key = { it.id }) { negocio ->
                                    NegocioListItem(
                                        negocio = negocio,
                                        onClick = { onNegocioClick(negocio.id) },
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