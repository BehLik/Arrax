package com.example.arrax.ui.screen.lobby.onboarding.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxTextButton
import com.example.arrax.core.designsystem.components.inputs.ArxTextField
import com.example.arrax.ui.screen.lobby.onboarding.LobbyOnboardingState

@Composable
fun CrearNegocioForm(
    state: LobbyOnboardingState,
    onNombreChanged: (String) -> Unit,
    onConfirmar: () -> Unit,
    onVolver: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text(text = "Crear mi negocio", style = MaterialTheme.typography.headlineSmall)
        Text(
            text = "Serás el administrador de este negocio",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp, top = 4.dp)
        )
        ArxTextField(
            value = state.nombreNegocio,
            onValueChange = onNombreChanged,
            label = "Nombre del negocio",
            isError = state.nombreNegocioError != null,
            errorMessage = state.nombreNegocioError,
            modifier = Modifier.fillMaxWidth()
        )
        state.errorGeneral?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        ArxPrimaryButton(
            text = "Crear negocio",
            onClick = onConfirmar,
            enabled = !state.isLoading,
            isLoading = state.isLoading,
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
        )
        ArxTextButton(text = "Volver", onClick = onVolver, enabled = !state.isLoading, modifier = Modifier.padding(top = 8.dp))
    }
}