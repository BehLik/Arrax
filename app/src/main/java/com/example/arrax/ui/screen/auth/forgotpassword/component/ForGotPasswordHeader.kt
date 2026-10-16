package com.example.arrax.ui.screen.auth.forgotpassword.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.arrax.core.designsystem.theme.ArxSpacing

@Composable
fun ForGotPasswordHeader() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Recuperar contraseña",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(ArxSpacing.xs))
        Text(
            text = "Te enviaremos un enlace para restablecer tu contraseña.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2
        )
        Spacer(Modifier.height(ArxSpacing.xl))
    }
}