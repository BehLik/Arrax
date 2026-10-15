package com.example.arrax.ui.screen.auth.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.ui.screen.auth.login.component.LoginActions
import com.example.arrax.ui.screen.auth.login.component.LoginForm
import com.example.arrax.ui.screen.auth.login.component.LoginHeader
import com.google.firebase.encoders.config.Configurator

@Composable
fun AuthLoginScreen(
    viewModel: AuthLoginViewmodel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    AuthLoginScreenPreviewContent(
        email = uiState.email,
        password = uiState.password,
        emailError = uiState.emailError,
        errorMessage = uiState.passwordError ?: uiState.errorGeneral,
        isLoading = uiState.isLoading,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = { viewModel.login(onLoginSuccess) },
        onRegisterClick = onNavigateToRegister,
        onForgotClick = onNavigateToForgotPassword
    )
}

@Composable
private fun AuthLoginScreenPreviewContent(
    email: String,
    password: String,
    emailError: String?,
    errorMessage: String?,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotClick: () -> Unit
) {
    ArxBackground(decoration = ArxDecoration.None) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginHeader()

            Spacer(modifier = Modifier.height(40.dp))

            LoginForm(
                email = email,
                password = password,
                emailError = emailError,
                errorMessage = errorMessage,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            LoginActions(
                isLoading = isLoading,
                isButtonEnabled = email.isNotBlank() &&
                        password.isNotBlank() &&
                        !isLoading,
                onLoginClick = onLoginClick,
                onRegisterClick = onRegisterClick,
                onForgotClick = onForgotClick
            )
        }
    }
}

@Preview(
    name = "Arrax - Login",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=393dp,height=852dp,dpi=440",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AuthLoginScreenPreview() {
    ArxTheme {
        AuthLoginScreenPreviewContent(
            email = "",
            password = "12345678",
            emailError = null,
            errorMessage = null,
            isLoading = false,
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onRegisterClick = {},
            onForgotClick = {}
        )
    }
}
@Preview(
    name = "Arrax - Login",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=393dp,height=852dp,dpi=440",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun AuthLoginScreen2Preview() {
    ArxTheme {
        AuthLoginScreenPreviewContent(
            email = "usuario@arrax.com",
            password = "12345678",
            emailError = null,
            errorMessage = null,
            isLoading = false,
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onRegisterClick = {},
            onForgotClick = {}
        )
    }
}