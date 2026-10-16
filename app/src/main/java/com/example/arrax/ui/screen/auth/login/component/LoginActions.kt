package com.example.arrax.ui.screen.auth.login.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxTextButton

@Composable
fun LoginActions(
    isLoading: Boolean,
    isButtonEnabled: Boolean,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ArxPrimaryButton(
            text = if (isLoading) "Iniciando sesión..." else "Iniciar sesión",
            onClick = onLoginClick,
            enabled = isButtonEnabled,
            loading = isLoading,
            hero = true
        )

        ArxTextButton(text = "¿Olvidaste tu contraseña?", onClick = onForgotClick)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿No tienes cuenta?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            ArxTextButton(text = "Crear cuenta", onClick = onRegisterClick)
        }
    }
}