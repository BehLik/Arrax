package com.example.arrax.ui.screen.auth.login.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.arrax.R
import com.example.arrax.core.designsystem.components.inputs.ArxPasswordField
import com.example.arrax.core.designsystem.components.inputs.ArxTextField
import com.example.arrax.core.designsystem.theme.ArxSpacing

@Composable
fun LoginForm(
    email: String,
    password: String,
    emailError: String?,
    passwordError: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ArxTextField(
            value = email,
            onValueChange = onEmailChange,
            label = stringResource(R.string.common_email_label),
            isError = emailError != null,
            errorMessage = emailError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(
            modifier = Modifier.height(ArxSpacing.md)
        )

        ArxPasswordField(
            value = password,
            onValueChange = onPasswordChange,
            label = stringResource(R.string.common_password_label),
            errorMessage = passwordError,
            imeAction = ImeAction.Done
        )
    }
}