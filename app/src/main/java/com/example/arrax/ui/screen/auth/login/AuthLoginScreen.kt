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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.brand.ArxLogo
import com.example.arrax.core.designsystem.components.brand.ArxLogoLayout
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.ui.screen.auth.login.component.LoginActions
import com.example.arrax.ui.screen.auth.login.component.LoginForm

@Composable
fun AuthLoginScreen(
    viewModel: AuthLoginViewmodel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    AuthLoginContent(
        email = uiState.email,
        password = uiState.password,
        emailError = uiState.emailError,
        passwordError = uiState.passwordError,
        generalError = uiState.errorGeneral,
        isLoading = uiState.isLoading,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = { viewModel.login(onLoginSuccess) },
        onRegisterClick = onNavigateToRegister,
        onForgotClick = onNavigateToForgotPassword
    )
}

@Composable
private fun AuthLoginContent(
    email: String,
    password: String,
    emailError: String?,
    passwordError: String?,
    generalError: String?,
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
                .padding(horizontal = ArxSpacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ArxLogo(layout = ArxLogoLayout.Stacked)

            Spacer(Modifier.height(ArxSpacing.huge))

            LoginForm(
                email = email,
                password = password,
                emailError = emailError,
                passwordError = passwordError,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange
            )

            if (generalError != null) {
                Spacer(Modifier.height(ArxSpacing.sm))
                Text(
                    text = generalError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(ArxSpacing.xxl))

            LoginActions(
                isLoading = isLoading,
                isButtonEnabled = email.isNotBlank() && password.isNotBlank(),
                onLoginClick = onLoginClick,
                onRegisterClick = onRegisterClick,
                onForgotClick = onForgotClick
            )
        }
    }
}

@Preview(
    name = "Arrax - Login Dark",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=393dp,height=852dp,dpi=440",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AuthLoginDarkPreview() {
    ArxTheme(darkTheme = true) {
        AuthLoginContent(
            email = "", password = "12345678",
            emailError = null, passwordError = null, generalError = null,
            isLoading = false,
            onEmailChange = {}, onPasswordChange = {},
            onLoginClick = {}, onRegisterClick = {}, onForgotClick = {}
        )
    }
}

@Preview(
    name = "Arrax - Login Light",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=393dp,height=852dp,dpi=440",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun AuthLoginLightPreview() {
    ArxTheme {
        AuthLoginContent(
            email = "usuario@arrax.com", password = "12345678",
            emailError = null, passwordError = null, generalError = null,
            isLoading = false,
            onEmailChange = {}, onPasswordChange = {},
            onLoginClick = {}, onRegisterClick = {}, onForgotClick = {}
        )
    }
}