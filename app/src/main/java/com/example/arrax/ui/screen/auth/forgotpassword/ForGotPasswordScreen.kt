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
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onNavigateBackToLogin: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ForgotPasswordContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onSendReset = viewModel::sendReset,
        onNavigateBackToLogin = onNavigateBackToLogin,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ForgotPasswordContent(
    uiState: ForGotPasswordState,
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
                    .padding(horizontal = ArxSpacing.xl) // 24.dp
            ) {
                Spacer(modifier = Modifier.height(ArxSpacing.xl)) // after the TopBar

                ForGotPasswordHeader()

                Spacer(modifier = Modifier.height(ArxSpacing.xxl)) // Header -> form (32.dp)

                ForGotPasswordForm(
                    email = uiState.email,
                    emailError = uiState.emailError,
                    errorGeneral = uiState.errorGeneral,
                    isSent = uiState.isSent,
                    onEmailChange = onEmailChange
                )

                Spacer(modifier = Modifier.height(ArxSpacing.huge)) // form -> buttons (40.dp)

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
private fun ForgotPasswordLightPreview() {
    ArxTheme {
        ForgotPasswordContent(
            uiState = ForGotPasswordState(email = "usuario@example.com"),
            onEmailChange = {}, onSendReset = {},
            onNavigateBackToLogin = {}, onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ForgotPasswordDarkPreview() {
    ArxTheme(darkTheme = true) {
        ForgotPasswordContent(
            uiState = ForGotPasswordState(isSent = true),
            onEmailChange = {}, onSendReset = {},
            onNavigateBackToLogin = {}, onNavigateBack = {}
        )
    }
}
