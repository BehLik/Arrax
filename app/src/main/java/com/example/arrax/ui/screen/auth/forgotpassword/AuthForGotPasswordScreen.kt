package com.example.arrax.ui.screen.auth.forgotpassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.components.inputs.ArxTextField

@Composable
fun AuthForGotPasswordScreen(
    viewModel: AuthForGotPasswordViewmodel = hiltViewModel(),
    onNavigateBackToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ArxBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Recuperar contraseña",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.isSent) {
                Text(
                    text = "Si el correo existe en nuestros registros, te enviamos un enlace para restablecer tu contraseña. Revisa tu bandeja de entrada (y spam).",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                ArxPrimaryButton(
                    text = "Volver a iniciar sesión",
                    onClick = onNavigateBackToLogin
                )
            } else {
                ArxTextField(
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChange,
                    label = "Correo",
                    isError = uiState.emailError != null,
                    errorMessage = uiState.emailError
                )

                uiState.errorGeneral?.let { message ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = message, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(24.dp))

                ArxPrimaryButton(
                    text = if (uiState.isLoading) "Enviando..." else "Enviar enlace",
                    onClick = viewModel::sendReset,
                    enabled = uiState.email.isNotBlank() && !uiState.isLoading
                )
            }
        }
    }
}