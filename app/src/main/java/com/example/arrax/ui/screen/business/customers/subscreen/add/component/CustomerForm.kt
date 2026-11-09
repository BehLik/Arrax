package com.example.arrax.ui.screen.business.customers.subscreen.add.component

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.example.arrax.core.common.MxCatalog
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.components.display.arxInitials
import com.example.arrax.core.designsystem.components.inputs.ArxAvatarPicker
import com.example.arrax.core.designsystem.components.inputs.ArxTextField
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.customers.subscreen.add.CustomerAddActions
import com.example.arrax.ui.screen.business.customers.subscreen.add.CustomerAddState

private const val REFERENCE_MAX = 100

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
    val pickImage = {
        picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    val field = Modifier.fillMaxWidth()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm),
    ) {
        ArxAvatarPicker(
            initials = arxInitials(state.name).ifEmpty { "?" },
            imageUrl = state.photo,
            onClick = pickImage,
        )
        ArxSecondaryButton(
            text = "Seleccionar imagen",
            onClick = pickImage,
            leadingIcon = { Icon(painterResource(ArxIcons.Photo), contentDescription = null) },
        )

        ArxTextField(
            value = state.name,
            onValueChange = actions.onNameChange,
            label = "Nombre *",
            placeholder = "Ej. Juan Pérez",
            modifier = field,
            leadingIcon = fieldIcon(ArxIcons.User),
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
            placeholder = "Ej. 999 123 4567",
            modifier = field,
            leadingIcon = fieldIcon(ArxIcons.Call),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
        )
        ArxTextField(
            value = state.address,
            onValueChange = actions.onAddressChange,
            label = "Calle y número (opcional)",
            placeholder = "Ej. Av. Principal #123",
            modifier = field,
            leadingIcon = fieldIcon(ArxIcons.Location),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
            ),
        )
        Row(
            modifier = field,
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm),
        ) {
            ArxDropdownField(
                value = state.municipality,
                label = "Municipio",
                options = MxCatalog.municipalities[state.state].orEmpty(),
                onSelected = actions.onMunicipalityChange,
                leadingIcon = ArxIcons.TripOrigin,
                modifier = Modifier.weight(1f),
            )
            ArxDropdownField(
                value = state.state,
                label = "Estado",
                options = MxCatalog.states,
                onSelected = actions.onStateChange,
                leadingIcon = ArxIcons.LocationShare,
                modifier = Modifier.weight(1f),
            )
        }
        ArxTextField(
            value = state.reference,
            onValueChange = actions.onReferenceChange,
            label = "Referencia (opcional)",
            placeholder = "Ej. Casa amarilla, en la esquina...",
            modifier = field,
            leadingIcon = fieldIcon(ArxIcons.Report),
            singleLine = false,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done,
            ),
        )
        Text(
            text = "${state.reference.length} / $REFERENCE_MAX",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.End,
        )
    }
}

/** Icono de campo con el color de acento del tema (primary). */
private fun fieldIcon(@DrawableRes icon: Int): @Composable () -> Unit = {
    ArxIcon(icon = icon, tint = MaterialTheme.colorScheme.primary)
}

/** Campo de solo selección: un overlay captura el toque para no abrir el teclado. */
@Composable
private fun ArxDropdownField(
    value: String,
    label: String,
    options: List<String>,
    onSelected: (String) -> Unit,
    @DrawableRes leadingIcon: Int,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier) {
        ArxTextField(
            value = value,
            onValueChange = {},
            label = label,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = fieldIcon(leadingIcon),
            trailingIcon = { Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null) },
        )
        Box(Modifier.matchParentSize().clickable { expanded = true })
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { onSelected(option); expanded = false },
                )
            }
        }
    }
}