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
fun UnirseNegocioForm(
    state: LobbyOnboardingState,
    onCodigoChanged: (String) -> Unit,
    onConfirmar: () -> Unit,
    onVolver: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text(text = "Unirme con código", style = MaterialTheme.typography.headlineSmall)
        Text(
            text = "Pide el código de invitación al administrador de tu negocio",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp, top = 4.dp)
        )
        ArxTextField(
            value = state.codigoInvitacion,
            onValueChange = onCodigoChanged,
            label = "Código de invitación",
            isError = state.codigoInvitacionError != null,
            errorMessage = state.codigoInvitacionError,
            modifier = Modifier.fillMaxWidth()
        )
        ArxPrimaryButton(
            text = "Unirme",
            onClick = onConfirmar,
            enabled = !state.isLoading,
            isLoading = state.isLoading,
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
        )
        ArxTextButton(text = "Volver", onClick = onVolver, enabled = !state.isLoading, modifier = Modifier.padding(top = 8.dp))
    }
}