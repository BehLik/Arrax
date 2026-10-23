package com.example.arrax.ui.screen.lobby.profile.changepassword.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.theme.ArxSpacing
import androidx.compose.ui.text.input.ImeAction
import com.example.arrax.core.designsystem.components.inputs.ArxPasswordField


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

        ArxPasswordField(
            value = currentPassword,
            onValueChange = onCurrentPasswordChange,
            label = "Contraseña actual",
            errorMessage = currentPasswordError,
            imeAction = ImeAction.Next
        )

        ArxPasswordField(
            value = newPassword,
            onValueChange = onNewPasswordChange,
            label = "Contraseña nueva",
            errorMessage = newPasswordError,
            imeAction = ImeAction.Next
        )

        ArxPasswordField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = "Confirmar contraseña nueva",
            errorMessage = confirmPasswordError,
            imeAction = ImeAction.Done
        )
    }
}