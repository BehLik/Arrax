package com.example.arrax.ui.screen.lobby.profile.changepassword.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.inputs.ArxTextField
import com.example.arrax.core.designsystem.theme.ArxSpacing
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.arrax.R
import com.example.arrax.core.designsystem.components.buttons.ArxTextButton


@Composable
fun ChangePasswordForm(
    currentPassword: String,
    newPassword: String,
    confirmPassword: String,
    currentPasswordError: String?,
    newPasswordError: String?,
    confirmPasswordError: String?,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
    ) {
        PasswordField(
            value = currentPassword,
            onValueChange = onCurrentPasswordChange,
            label = "Contraseña actual",
            error = currentPasswordError,
            imeAction = ImeAction.Next
        )
        PasswordField(
            value = newPassword,
            onValueChange = onNewPasswordChange,
            label = "Contraseña nueva",
            error = newPasswordError,
            imeAction = ImeAction.Next
        )
        PasswordField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = "Confirmar contraseña nueva",
            error = confirmPasswordError,
            imeAction = ImeAction.Done
        )
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?,
    imeAction: ImeAction
) {
    var visible by remember { mutableStateOf(false) }
    val toggleDescription = stringResource(
        if (visible) R.string.auth_hide_password else R.string.auth_show_password
    )

    ArxTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        isError = error != null,
        errorMessage = error,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
        trailingIcon = {
            ArxTextButton(
                text = if (visible) "Ocultar" else "Mostrar",
                onClick = { visible = !visible },
                modifier = Modifier.semantics { contentDescription = toggleDescription }
            )
        }
    )
}