package com.example.arrax.ui.screen.auth.verifyemail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.theme.ArxSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailScreen(
    viewModel: VerifyEmailViewModel = hiltViewModel(),
    onVerified: () -> Unit,

    onCancel: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.check(onVerified, silent = true)
    }

    ArxBackground(decoration = ArxDecoration.None) {
        Column(Modifier.fillMaxSize()) {
            ArxTopBar(
                title = "",
                navigationType = ArxTopBarNavigationType.BACK,
                onNavigationClick = { viewModel.cancel(onCancel) },
                centeredTitle = false
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(horizontal = ArxSpacing.lg)
            ) {
                Text("Verifica tu correo", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(ArxSpacing.sm))
                Text(
                    "Enviamos un enlace a ${state.email}. Ábrelo y vuelve aquí.",
                    style = MaterialTheme.typography.bodyMedium
                )

                (state.errorGeneral ?: state.infoMessage)?.let { msg ->
                    Spacer(Modifier.height(ArxSpacing.sm))
                    Text(
                        text = msg,
                        color = if (state.errorGeneral != null) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(Modifier.height(ArxSpacing.xl))
                ArxPrimaryButton(
                    text = "Ya verifiqué",
                    onClick = { viewModel.check(onVerified) },
                    loading = state.isChecking
                )
                Spacer(Modifier.height(ArxSpacing.sm))
                ArxSecondaryButton(
                    text = if (state.resendCooldown > 0) "Reenviar en ${state.resendCooldown}s" else "Reenviar correo",
                    onClick = viewModel::resend,
                    enabled = state.resendCooldown == 0,
                    loading = state.isResending
                )
            }
        }
    }
}