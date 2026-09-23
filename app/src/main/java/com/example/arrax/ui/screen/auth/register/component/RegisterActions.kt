package com.example.arrax.ui.screen.auth.register.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxTextButton

@Composable
fun RegisterActions(
    isLoading: Boolean,
    isButtonEnabled: Boolean,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ArxPrimaryButton(
            text = if (isLoading) "Creando cuenta..." else "Crear cuenta",
            onClick = onRegisterClick,
            enabled = isButtonEnabled && !isLoading,
            leadingIcon = if (isLoading) {
                {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            } else null
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿Ya tienes cuenta?")
            ArxTextButton(text = "Inicia sesión", onClick = onLoginClick)
        }
    }
}