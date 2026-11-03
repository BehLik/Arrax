package com.example.arrax.ui.screen.business.customers.subscreen.add.component

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.example.arrax.core.designsystem.components.display.arxInitials
import com.example.arrax.core.designsystem.components.inputs.ArxAvatarPicker
import com.example.arrax.core.designsystem.components.inputs.ArxTextField
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.customers.subscreen.add.CustomerAddActions
import com.example.arrax.ui.screen.business.customers.subscreen.add.CustomerAddState

@Composable
internal fun CustomerForm(
    state: CustomerAddState,
    actions: CustomerAddActions,
    modifier: Modifier = Modifier,
) {
    // Photo Picker del sistema: no requiere permisos.
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { actions.onPhotoPicked(it.toString()) }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm),
    ) {
        ArxAvatarPicker(
            initials = arxInitials(state.name).ifEmpty { "?" },
            imageUrl = state.photo,
            onClick = {
                picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
        )
        Spacer(Modifier.height(ArxSpacing.xs))

        ArxTextField(
            value = state.name,
            onValueChange = actions.onNameChange,
            label = "Nombre *",
            isError = state.nameError != null,
            errorMessage = state.nameError,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
            ),
        )
        ArxTextField(
            value = state.phone,
            onValueChange = actions.onPhoneChange,
            label = "Teléfono (opcional)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
        )
        ArxTextField(
            value = state.address,
            onValueChange = actions.onAddressChange,
            label = "Dirección (opcional)",
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next,
            ),
        )
        ArxTextField(
            value = state.reference,
            onValueChange = actions.onReferenceChange,
            label = "Referencia (opcional)",
            placeholder = "Ej. En la carretera, casa amarilla",
            singleLine = false,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done,
            ),
        )
    }
}