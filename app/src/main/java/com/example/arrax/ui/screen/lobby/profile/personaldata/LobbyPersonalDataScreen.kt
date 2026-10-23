package com.example.arrax.ui.screen.lobby.profile.personaldata


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
import com.example.arrax.ui.screen.lobby.profile.personaldata.component.PersonalDataAvatar
import com.example.arrax.ui.screen.lobby.profile.personaldata.component.PersonalDataForm

@Composable
fun LobbyPersonalDataScreen(
    onBack: () -> Unit,
    viewModel: LobbyPersonalDataViewmodel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LobbyPersonalDataContent(
        state = state,
        onNameChange = viewModel::onNameChange,
        onSaveClick = { viewModel.save(onSaved = onBack) },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LobbyPersonalDataContent(
    state: LobbyPersonalDataState,
    onNameChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBack: () -> Unit
) {
    ArxBackground(decoration = ArxDecoration.None) {
        Column(modifier = Modifier.fillMaxSize()) {
            ArxTopBar(
                title = "Datos personales",
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
                PersonalDataAvatar(isGoogleAccount = state.isGoogleAccount)

                Spacer(Modifier.height(ArxSpacing.xl))

                PersonalDataForm(
                    name = state.name,
                    nameError = state.nameError,
                    email = state.email,
                    onNameChange = onNameChange
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
                    text = "Guardar cambios",
                    onClick = onSaveClick,
                    enabled = state.canSave,
                    hero = true
                )

                Spacer(Modifier.height(ArxSpacing.xl))
            }
        }
    }
}

private val previewState = LobbyPersonalDataState(
    name = "Diana López", originalName = "Diana López",
    email = "diana@correo.com", isGoogleAccount = true, isLoaded = true
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PersonalDataLightPreview() {
    ArxTheme {
        LobbyPersonalDataContent(previewState, onNameChange = {}, onSaveClick = {}, onBack = {})
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PersonalDataDarkPreview() {
    ArxTheme(darkTheme = true) {
        LobbyPersonalDataContent(
            previewState.copy(name = "", nameError = "Ingresa tu nombre"),
            onNameChange = {}, onSaveClick = {}, onBack = {}
        )
    }
}