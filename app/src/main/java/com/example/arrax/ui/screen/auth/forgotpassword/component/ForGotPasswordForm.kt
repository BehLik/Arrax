package com.example.arrax.ui.screen.auth.forgotpassword.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.arrax.core.designsystem.components.inputs.ArxTextField
import com.example.arrax.core.designsystem.theme.ArxSpacing

@Composable
fun ForGotPasswordForm(
    email: String,
    emailError: String?,
    errorGeneral: String?,
    isSent: Boolean,
    onEmailChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (isSent) {
            // Mensaje neutro: no revela si el correo está registrado.
            Text(
                text = "Si el correo existe en nuestros registros, te enviamos un enlace para restablecer tu contraseña. Revisa tu bandeja de entrada (y spam).",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        } else {
            ArxTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Correo electrónico",
                isError = emailError != null,
                errorMessage = emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                )
            )

            errorGeneral?.let { message ->
                Spacer(Modifier.height(ArxSpacing.sm))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(Modifier.height(ArxSpacing.md))
    }
}