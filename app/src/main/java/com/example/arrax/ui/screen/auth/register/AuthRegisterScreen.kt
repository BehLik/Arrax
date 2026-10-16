package com.example.arrax.ui.screen.auth.register

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.ui.screen.auth.register.component.RegisterActions
import com.example.arrax.ui.screen.auth.register.component.RegisterForm
import com.example.arrax.ui.screen.auth.register.component.RegisterHeader

@Composable
fun AuthRegisterScreen(
    viewModel: AuthRegisterViewmodel = hiltViewModel(),
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    AuthRegisterContent(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onRegisterClick = { viewModel.register(onRegisterSuccess) },
        onLoginClick = onNavigateToLogin,
        onBackClick = onNavigateBack
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AuthRegisterContent(
    uiState: AuthRegisterState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit
) {
    ArxBackground(decoration = ArxDecoration.None) {
        Column(modifier = Modifier.fillMaxSize()) {
            ArxTopBar(
                title = "",
                navigationType = ArxTopBarNavigationType.BACK,
                onNavigationClick = onBackClick,
                centeredTitle = false
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .navigationBarsPadding()
                    .padding(horizontal = ArxSpacing.lg)
            ) {
                RegisterHeader()

                Spacer(Modifier.height(ArxSpacing.xl))

                RegisterForm(
                    name = uiState.name,
                    email = uiState.email,
                    password = uiState.password,
                    confirmPassword = uiState.confirmPassword,
                    nameError = uiState.nameError,
                    emailError = uiState.emailError,
                    passwordError = uiState.passwordError,
                    confirmPasswordError = uiState.confirmPasswordError,
                    onNameChange = onNameChange,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onConfirmPasswordChange = onConfirmPasswordChange
                )

                uiState.errorGeneral?.let { message ->
                    Spacer(Modifier.height(ArxSpacing.sm))
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(Modifier.height(ArxSpacing.md))

                RegisterActions(
                    isLoading = uiState.isLoading,
                    isButtonEnabled = uiState.name.isNotBlank() &&
                            uiState.email.isNotBlank() &&
                            uiState.password.isNotBlank() &&
                            uiState.confirmPassword.isNotBlank(),
                    onRegisterClick = onRegisterClick,
                    onLoginClick = onLoginClick
                )

                Spacer(Modifier.height(ArxSpacing.xl))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AuthRegisterLightPreview() {
    ArxTheme {
        AuthRegisterContent(
            uiState = AuthRegisterState(),
            onNameChange = {}, onEmailChange = {},
            onPasswordChange = {}, onConfirmPasswordChange = {},
            onRegisterClick = {}, onLoginClick = {}, onBackClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AuthRegisterDarkPreview() {
    ArxTheme(darkTheme = true) {
        AuthRegisterContent(
            uiState = AuthRegisterState(name = "Juan Pérez", email = "correo-invalido", emailError = "Formato de correo incorrecto"),
            onNameChange = {}, onEmailChange = {},
            onPasswordChange = {}, onConfirmPasswordChange = {},
            onRegisterClick = {}, onLoginClick = {}, onBackClick = {}
        )
    }
}