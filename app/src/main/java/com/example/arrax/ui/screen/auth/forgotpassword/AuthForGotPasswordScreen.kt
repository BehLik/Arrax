package com.example.arrax.ui.screen.auth.forgotpassword

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.arrax.ui.screen.auth.forgotpassword.component.ForGotPasswordActions
import com.example.arrax.ui.screen.auth.forgotpassword.component.ForGotPasswordForm
import com.example.arrax.ui.screen.auth.forgotpassword.component.ForGotPasswordHeader

@Composable
fun AuthForGotPasswordScreen(
    viewModel: AuthForGotPasswordViewmodel = hiltViewModel(),
    onNavigateBackToLogin: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    AuthForGotPasswordContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onSendReset = viewModel::sendReset,
        onNavigateBackToLogin = onNavigateBackToLogin,
        onNavigateBack = onNavigateBack
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AuthForGotPasswordContent(
    uiState: AuthForGotPasswordState,
    onEmailChange: (String) -> Unit,
    onSendReset: () -> Unit,
    onNavigateBackToLogin: () -> Unit,
    onNavigateBack: () -> Unit
) {
    ArxBackground(decoration = ArxDecoration.Mountains) {
        Column(modifier = Modifier.fillMaxSize()) {
            ArxTopBar(
                title = "",
                navigationType = ArxTopBarNavigationType.BACK,
                onNavigationClick = onNavigateBack,
                centeredTitle = false
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = ArxSpacing.xl) // Incrementado el padding lateral a xl (24.dp)
            ) {
                Spacer(modifier = Modifier.height(ArxSpacing.xl)) // Espacio después de la TopBar

                ForGotPasswordHeader()

                Spacer(modifier = Modifier.height(ArxSpacing.xxl)) // Espacio entre Header y Formulario (32.dp)

                ForGotPasswordForm(
                    email = uiState.email,
                    emailError = uiState.emailError,
                    errorGeneral = uiState.errorGeneral,
                    isSent = uiState.isSent,
                    onEmailChange = onEmailChange
                )

                Spacer(modifier = Modifier.height(ArxSpacing.huge)) // Espacio entre Formulario y Botones (40.dp)

                ForGotPasswordActions(
                    isLoading = uiState.isLoading,
                    isSent = uiState.isSent,
                    isButtonEnabled = uiState.email.isNotBlank(),
                    onSendReset = onSendReset,
                    onNavigateBackToLogin = onNavigateBackToLogin
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AuthForGotPasswordLightPreview() {
    ArxTheme {
        AuthForGotPasswordContent(
            uiState = AuthForGotPasswordState(email = "usuario@example.com"),
            onEmailChange = {}, onSendReset = {},
            onNavigateBackToLogin = {}, onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AuthForGotPasswordDarkPreview() {
    ArxTheme(darkTheme = true) {
        AuthForGotPasswordContent(
            uiState = AuthForGotPasswordState(isSent = true),
            onEmailChange = {}, onSendReset = {},
            onNavigateBackToLogin = {}, onNavigateBack = {}
        )
    }
}