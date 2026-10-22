package com.example.arrax.ui.screen.lobby.join.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.extendedColors

/**
 * Divisor + campo de código + botón "Unirse".
 * TODO: sustituir OutlinedTextField/Button por ArxTextField/ArxPrimaryButton al confirmar sus firmas.
 */
@Composable
fun JoinCodeForm(
    code: String,
    isLoading: Boolean,
    errorMessage: String?,
    onCodeChange: (String) -> Unit,
    onJoin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val canJoin = code.isNotBlank() && !isLoading

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.lg)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.extendedColors.glassBorder
            )
            Text(
                text = "O ingresa el código manualmente",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.extendedColors.glassBorder
            )
        }

        OutlinedTextField(
            value = code,
            onValueChange = onCodeChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Código del negocio") },
            singleLine = true,
            enabled = !isLoading,
            isError = errorMessage != null,
            supportingText = errorMessage?.let { { Text(it, style = MaterialTheme.typography.bodySmall) } },
            shape = MaterialTheme.shapes.small,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { if (canJoin) onJoin() })
        )

        Button(
            onClick = onJoin,
            enabled = canJoin,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.defaultMinSize(minWidth = 20.dp, minHeight = 20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Unirse")
            }
        }
    }
}