package com.example.arrax.ui.screen.lobby.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.R
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.display.ArxStepLoading
import com.example.arrax.core.designsystem.components.inputs.ArxTextField
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIconBadge
import com.example.arrax.core.designsystem.icons.ArxIconShape
import com.example.arrax.core.designsystem.icons.ArxIconStyle
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.extendedColors
import com.example.arrax.domain.usecase.business.create.CreateBusinessUseCase
import com.example.arrax.ui.screen.lobby.create.component.CreateHeaderBusiness
import com.example.arrax.ui.screen.lobby.create.component.ErrorBanner
import com.example.arrax.ui.screen.lobby.create.component.OwnerPrivilegesCard

@Composable
fun LobbyBusinessCreateScreen(
    onBack: () -> Unit,
    onCreated: (businessId: String) -> Unit,
    viewModel: LobbyBusinessCreateViewmodel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val nameFocus = remember { FocusRequester() }

    LaunchedEffect(Unit) { viewModel.created.collect(onCreated) }
    LaunchedEffect(Unit) { nameFocus.requestFocus() }

    val submit = {
        focusManager.clearFocus()
        viewModel.onSubmit()
    }

    ArxBackground(decoration = ArxDecoration.Mountains) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .imePadding()
            ) {
                // ── Top bar ──
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ArxSpacing.sm, vertical = ArxSpacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack, enabled = !state.isLoading) {
                        ArxIcon(
                            icon = ArxIcons.Back,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                // ── Contenido ──
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = ArxSpacing.xl),
                    verticalArrangement = Arrangement.spacedBy(ArxSpacing.xl)
                ) {
                    CreateHeaderBusiness()

                    Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                        ArxTextField(
                            value = state.name,
                            onValueChange = viewModel::onNameChange,
                            label = "Nombre del negocio",
                            placeholder = "Ej. Familia Pérez",
                            leadingIcon = {
                                ArxIcon(icon = ArxIcons.Storage, size = ArxSpacing.xl)
                            },
                            isError = state.nameError != null,
                            errorMessage = state.nameError,
                            enabled = !state.isLoading,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = { if (state.canSubmit) submit() }),
                            modifier = Modifier.focusRequester(nameFocus)
                        )
                        Text(
                            text = "${state.name.length}/${CreateBusinessUseCase.MAX_NAME}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    OwnerPrivilegesCard()

                    state.error?.let { ErrorBanner(it) }
                }

                // ── CTA fijo abajo ──
                ArxPrimaryButton(
                    text = "Crear negocio",
                    onClick = submit,
                    enabled = state.canSubmit,
                    loading = state.isLoading,
                    hero = true,
                    modifier = Modifier.padding(ArxSpacing.xl)
                )
            }
            ArxStepLoading(
                visible = state.isLoading,
                steps = stringArrayResource(R.array.loading_create_business_steps).toList(),
                doneMessage = stringResource(R.string.loading_create_business_done),
                isDone = state.isSuccess
            )
        }
    }
}






