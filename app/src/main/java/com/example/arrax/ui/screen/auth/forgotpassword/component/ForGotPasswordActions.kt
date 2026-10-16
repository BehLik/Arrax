package com.example.arrax.ui.screen.auth.forgotpassword.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxTextButton
import com.example.arrax.core.designsystem.theme.ArxSpacing

@Composable
fun ForGotPasswordActions(
    isLoading: Boolean,
    isSent: Boolean,
    isButtonEnabled: Boolean,
    onSendReset: () -> Unit,
    onNavigateBackToLogin: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isSent) {
            ArxPrimaryButton(
                text = "Volver a iniciar sesión",
                onClick = onNavigateBackToLogin,
                hero = true
            )
        } else {
            ArxPrimaryButton(
                text = if (isLoading) "Enviando..." else "Enviar enlace",
                onClick = onSendReset,
                enabled = isButtonEnabled,
                loading = isLoading,
                hero = true
            )

            Spacer(Modifier.height(ArxSpacing.xs))

            ArxTextButton(
                text = "Volver a iniciar sesión",
                onClick = onNavigateBackToLogin
            )
        }
    }
}