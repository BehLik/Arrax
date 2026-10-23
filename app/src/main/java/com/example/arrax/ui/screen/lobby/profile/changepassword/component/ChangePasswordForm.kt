package com.example.arrax.ui.screen.lobby.profile.changepassword.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.inputs.ArxTextField
import com.example.arrax.core.designsystem.theme.ArxSpacing

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
        // AJUSTA a la firma real de tu ArxTextField (isPassword = enmascarado + ojo)
        ArxTextField(
            value = currentPassword,
            onValueChange = onCurrentPasswordChange,
            label = "Contraseña actual",
            isError = currentPasswordError,
            isPassword = true
        )
        ArxTextField(
            value = newPassword,
            onValueChange = onNewPasswordChange,
            label = "Contraseña nueva",
            isError = newPasswordError,
            isPassword = true
        )
        ArxTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = "Confirmar contraseña nueva",
            isError = confirmPasswordError,
            isPassword = true
        )
    }
}