package com.example.arrax.core.designsystem.components.inputs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

/**
 * Campo de texto base de Arrax: superficie glass sutil, borde outline -> primary al enfocar,
 * error con mensaje debajo y estado deshabilitado con extendedColors.disabled.
 *
 * [enabled] y [readOnly] son nuevos y opcionales (ArxDropdown reutiliza este campo con readOnly = true).
 */
@Composable
fun ArxTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
    val glass = MaterialTheme.extendedColors.glass
    val disabled = MaterialTheme.extendedColors.disabled
    val placeholderColor = MaterialTheme.colorScheme.onSurfaceVariant

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        readOnly = readOnly,
        label = { Text(text = label, style = MaterialTheme.typography.bodyMedium) },
        placeholder = if (placeholder != null) {
            { Text(text = placeholder, style = MaterialTheme.typography.bodyMedium) }
        } else {
            null
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        // El mensaje de error va en supportingText: TalkBack lo lee junto al campo.
        supportingText = if (isError && errorMessage != null) {
            { Text(text = errorMessage, style = MaterialTheme.typography.bodySmall) }
        } else {
            null
        },
        isError = isError,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        shape = MaterialTheme.shapes.small,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            errorBorderColor = MaterialTheme.colorScheme.error,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            errorLabelColor = MaterialTheme.colorScheme.error,
            focusedContainerColor = glass,
            unfocusedContainerColor = glass,
            errorContainerColor = glass,
            disabledContainerColor = glass,
            focusedPlaceholderColor = placeholderColor,
            unfocusedPlaceholderColor = placeholderColor,
            disabledBorderColor = disabled,
            disabledTextColor = disabled,
            disabledLabelColor = disabled,
            disabledPlaceholderColor = disabled,
            disabledLeadingIconColor = disabled,
            disabledTrailingIconColor = disabled
        ),
        textStyle = MaterialTheme.typography.bodyLarge
    )
}

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

@Composable
private fun ArxInputsPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(ArxSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
            ) {
                ArxTextField(
                    value = "Juan Pérez",
                    onValueChange = {},
                    label = "Nombre completo"
                )
                ArxTextField(
                    value = "correo-invalido",
                    onValueChange = {},
                    label = "Correo electrónico",
                    isError = true,
                    errorMessage = "Formato de correo incorrecto"
                )
                ArxTextField(
                    value = "No editable",
                    onValueChange = {},
                    label = "Deshabilitado",
                    enabled = false
                )
                ArxSearchField(
                    query = "Costilla",
                    onQueryChange = {}
                )
                ArxDropdown(
                    selectedOption = "Costilla",
                    options = listOf("Carne", "Costilla", "Espinazo"),
                    onOptionSelected = {},
                    label = "Selecciona el corte"
                )
            }
        }
    }
}

@Preview(name = "Inputs – Light", showBackground = false)
@Composable
fun ArxInputsPreviewLight() = ArxInputsPreviewContent()

@Preview(
    name = "Inputs – Dark",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ArxInputsPreviewDark() = ArxInputsPreviewContent()