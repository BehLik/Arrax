package com.example.arrax.ui.screen.auth.login.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxTextButton
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing

@Composable
fun LoginActions(
    isLoading: Boolean,
    isGoogleLoading: Boolean,
    isButtonEnabled: Boolean,
    onLoginClick: () -> Unit,
    onGoogleClick: () -> Unit,
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
            enabled = isButtonEnabled && !isGoogleLoading,
            loading = isLoading,
            hero = true
        )

        Spacer(Modifier.height(ArxSpacing.sm))

        ArxSecondaryButton(
            text = "Continuar con Google",
            onClick = onGoogleClick,
            enabled = !isLoading,
            loading = isGoogleLoading,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = ArxIcons.Google),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
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