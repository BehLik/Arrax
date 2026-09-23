package com.example.arrax.ui.screen.lobby.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.cards.ArxActionCard
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.ui.screen.lobby.onboarding.component.CrearNegocioForm
import com.example.arrax.ui.screen.lobby.onboarding.component.UnirseNegocioForm

@Composable
fun LobbyOnboardingScreen(
    onOnboardingCompletado: () -> Unit,
    viewModel: LobbyOnboardingViewmodel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onboardingCompletado.collect { onOnboardingCompletado() }
    }

    ArxBackground(modifier = Modifier.fillMaxSize()) {
        when {
            state.isResolviendoTenant -> ArxLoading(message = "Verificando cuenta...")
            state.modo == OnboardingModo.SELECCION -> SeleccionModo(
                onCrearClick = { viewModel.onModoSeleccionado(OnboardingModo.CREAR_NEGOCIO) },
                onUnirseClick = { viewModel.onModoSeleccionado(OnboardingModo.UNIRSE_NEGOCIO) }
            )
            state.modo == OnboardingModo.CREAR_NEGOCIO -> CrearNegocioForm(
                state = state,
                onNombreChanged = viewModel::onNombreNegocioChanged,
                onConfirmar = viewModel::onCrearNegocio,
                onVolver = viewModel::onVolverASeleccion
            )
            state.modo == OnboardingModo.UNIRSE_NEGOCIO -> UnirseNegocioForm(
                state = state,
                onCodigoChanged = viewModel::onCodigoInvitacionChanged,
                onConfirmar = viewModel::onUnirseNegocio,
                onVolver = viewModel::onVolverASeleccion
            )
        }
    }
}

@Composable
private fun SeleccionModo(onCrearClick: () -> Unit, onUnirseClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Bienvenido a Arrax", style = MaterialTheme.typography.headlineSmall)
        Text(
            text = "¿Cómo quieres empezar?",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 24.dp, top = 8.dp)
        )
        ArxActionCard(
            title = "Crear mi negocio",
            description = "Empieza un negocio nuevo y administra tus propios eventos",
            icon = ArxIcons.Add,
            onClick = onCrearClick
        )
        ArxActionCard(
            title = "Unirme con código",
            description = "Tu familia o negocio ya usa Arrax y tienes un código de invitación",
            icon = ArxIcons.GroupAdd,
            onClick = onUnirseClick,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}