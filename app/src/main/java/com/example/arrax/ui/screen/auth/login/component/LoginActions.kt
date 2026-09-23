package com.example.arrax.ui.screen.auth.login.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.arrax.R
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxTextButton


@Composable
fun LoginActions(
    isLoading: Boolean,
    isButtonEnabled: Boolean,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        } else {
            ArxPrimaryButton(
                text = stringResource(id = R.string.auth_login_label),
                enabled = isButtonEnabled,
                onClick = onLoginClick
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        ArxTextButton(
            text = stringResource(id = R.string.auth_login_registro_label),
            onClick = onRegisterClick
        )

        ArxTextButton(
            text = stringResource(id = R.string.auth_login_forgot_password),
            onClick = onForgotClick
        )
    }
}