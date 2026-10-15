package com.example.arrax.ui.screen.auth.register

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
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.ui.screen.auth.register.component.RegisterActions
import com.example.arrax.ui.screen.auth.register.component.RegisterForm
import com.example.arrax.ui.screen.auth.register.component.RegisterHeader

@Composable
fun AuthRegisterScreen(
    viewModel: AuthRegisterViewmodel = hiltViewModel(),
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    AuthRegisterContent(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onRegisterClick = { viewModel.register(onRegisterSuccess) },
        onLoginClick = onNavigateToLogin
    )
}

@Composable
internal fun AuthRegisterContent(
    uiState: AuthRegisterState, // Asegúrate de que este sea el nombre de tu clase de estado
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    ArxBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RegisterHeader()

            Spacer(modifier = Modifier.height(32.dp))

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
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = message, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(24.dp))

            RegisterActions(
                isLoading = uiState.isLoading,
                isButtonEnabled = !uiState.isLoading,
                onRegisterClick = onRegisterClick,
                onLoginClick = onLoginClick
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AuthRegisterScreenPreview() {
    // Aquí puedes simular el estado inicial o con datos de prueba
    val previewState = AuthRegisterState(
        name = "",
        email = "",
        password = "",
        confirmPassword = "",
        isLoading = false
    )

    ArxTheme {
        AuthRegisterContent(
            uiState = previewState,
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onRegisterClick = {},
            onLoginClick = {}
        )
    }
}