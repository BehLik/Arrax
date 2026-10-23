package com.example.arrax.ui.screen.lobby.profile.changepassword

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
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.ui.screen.lobby.profile.changepassword.component.ChangePasswordForm
import com.example.arrax.ui.screen.lobby.profile.changepassword.component.ChangePasswordSuccess

@Composable
fun LobbyChangePasswordScreen(
    onBack: () -> Unit,
    viewModel: LobbyChangePasswordViewmodel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LobbyChangePasswordContent(
        state = state,
        onCurrentPasswordChange = viewModel::onCurrentPasswordChange,
        onNewPasswordChange = viewModel::onNewPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onSubmit = viewModel::submit,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LobbyChangePasswordContent(
    state: LobbyChangePasswordState,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    ArxBackground(decoration = ArxDecoration.None) {
        Column(modifier = Modifier.fillMaxSize()) {
            ArxTopBar(
                title = "Cambiar contraseña",
                navigationType = ArxTopBarNavigationType.BACK,
                onNavigationClick = onBack,
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
                if (state.isSuccess) {
                    Spacer(Modifier.height(ArxSpacing.xxl))
                    ChangePasswordSuccess(onDone = onBack)
                } else {
                    Text(
                        text = "Por seguridad, confirma tu contraseña actual antes de elegir una nueva.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(ArxSpacing.xl))

                    ChangePasswordForm(
                        currentPassword = state.currentPassword,
                        newPassword = state.newPassword,
                        confirmPassword = state.confirmPassword,
                        currentPasswordError = state.currentPasswordError,
                        newPasswordError = state.newPasswordError,
                        confirmPasswordError = state.confirmPasswordError,
                        onCurrentPasswordChange = onCurrentPasswordChange,
                        onNewPasswordChange = onNewPasswordChange,
                        onConfirmPasswordChange = onConfirmPasswordChange
                    )

                    state.errorGeneral?.let { message ->
                        Spacer(Modifier.height(ArxSpacing.sm))
                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(Modifier.height(ArxSpacing.xl))

                    ArxPrimaryButton(
                        text = "Actualizar contraseña",
                        onClick = onSubmit,
                        enabled = state.canSubmit,
                        loading = state.isLoading,
                        hero = true
                    )
                }

                Spacer(Modifier.height(ArxSpacing.xl))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ChangePasswordLightPreview() {
    ArxTheme {
        LobbyChangePasswordContent(
            state = LobbyChangePasswordState(isSuccess = true),
            onCurrentPasswordChange = {}, onNewPasswordChange = {},
            onConfirmPasswordChange = {}, onSubmit = {}, onBack = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ChangePasswordDarkPreview() {
    ArxTheme(darkTheme = true) {
        LobbyChangePasswordContent(
            state = LobbyChangePasswordState(
                currentPassword = "123456", newPassword = "abc", confirmPassword = "abd",
                newPasswordError = "La contraseña debe tener al menos 6 caracteres",
                confirmPasswordError = "Las contraseñas no coinciden"
            ),
            onCurrentPasswordChange = {}, onNewPasswordChange = {},
            onConfirmPasswordChange = {}, onSubmit = {}, onBack = {}
        )
    }
}