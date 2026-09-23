package com.example.arrax.ui.screen.auth.login

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.ui.screen.auth.login.component.LoginActions
import com.example.arrax.ui.screen.auth.login.component.LoginForm
import com.example.arrax.ui.screen.auth.login.component.LoginHeader

@Composable
fun AuthLoginScreen(
    viewModel: AuthLoginViewmodel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ArxBackground {
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
                email = uiState.email,
                password = uiState.password,
                emailError = uiState.emailError,
                errorMessage = uiState.passwordError ?: uiState.errorGeneral,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange
            )

            uiState.errorGeneral?.let { message ->
                Spacer(modifier = Modifier.height(12.dp))
                // TODO: swap por el componente de error del design system si existe
                Text(text = message, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(32.dp))

            LoginActions(
                isLoading = uiState.isLoading,
                isButtonEnabled = uiState.email.isNotBlank() && uiState.password.isNotBlank() && !uiState.isLoading,
                onLoginClick = { viewModel.login(onLoginSuccess) },
                onRegisterClick = onNavigateToRegister,
                onForgotClick = onNavigateToForgotPassword
            )
        }
    }
}
