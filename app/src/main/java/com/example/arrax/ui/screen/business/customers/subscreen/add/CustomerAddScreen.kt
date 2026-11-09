package com.example.arrax.ui.screen.business.customers.subscreen.add

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.customers.subscreen.add.component.CustomerForm

/** Sirve para agregar (customerId == null) y para editar. */
@Composable
fun CustomerAddScreen(
    onBack: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CustomerAddViewmodel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val actions = remember(viewModel, onBack) {
        CustomerAddActions(
            onBack = onBack,
            onNameChange = viewModel::onNameChange,
            onPhoneChange = viewModel::onPhoneChange,
            onAddressChange = viewModel::onAddressChange,
            onReferenceChange = viewModel::onReferenceChange,
            onPhotoPicked = viewModel::onPhotoPicked,
            onSave = viewModel::onSave,
            onMessageShown = viewModel::onMessageShown,
            onMunicipalityChange = viewModel::onMunicipalityChange,
            onStateChange = viewModel::onStateChange,
        )
    }

    LaunchedEffect(state.finished) { if (state.finished) onDone() }

    CustomerAddContent(state, actions, modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CustomerAddContent(
    state: CustomerAddState,
    actions: CustomerAddActions,
    modifier: Modifier = Modifier,
) {
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(state.message) {
        state.message?.let { snackbar.showSnackbar(it); actions.onMessageShown() }
    }

    ArxBackground(modifier, decoration = ArxDecoration.None) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize().imePadding()) {
                ArxTopBar(
                    title = if (state.isEditing) "Editar cliente" else "Nuevo cliente",
                    centeredTitle = false,
                    navigationType = ArxTopBarNavigationType.BACK,
                    onNavigationClick = actions.onBack,
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.sm),
                ) {
                    CustomerForm(state, actions)
                }

                ArxPrimaryButton(
                    text = if (state.isEditing) "Actualizar" else "Guardar cliente",
                    onClick = actions.onSave,
                    loading = state.isSaving,
                    hero = true,
                    modifier = Modifier.padding(ArxSpacing.md),
                )
            }
            SnackbarHost(snackbar, Modifier.align(Alignment.BottomCenter))
        }
    }
}