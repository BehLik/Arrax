package com.example.arrax.ui.screen.business.event.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.inputs.ArxQuantitySelector
import com.example.arrax.core.designsystem.components.inputs.ArxTextField
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIconBadge
import com.example.arrax.core.designsystem.icons.ArxIconShape
import com.example.arrax.core.designsystem.icons.ArxIconStyle
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DataStep(
    name: String,
    dateMillis: Long?,
    animalCount: Int,
    onNameChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onAnimalCountChange: (Int) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var showPicker by remember { mutableStateOf(false) }

    if (showPicker) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = dateMillis)
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let(onDateChange)
                    showPicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = { TextButton(onClick = { showPicker = false }) { Text("Cancelar") } }
        ) { DatePicker(state = pickerState) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.sm),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.xl)
    ) {
        // Placeholder de la ilustración del cerdo 3D: reemplaza por tu drawable cuando lo tengas.
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
        ) {
            // Placeholder del cerdo 3D: reemplaza por tu drawable cuando lo tengas.
            ArxIconBadge(
                icon = ArxIcons.Event,
                style = ArxIconStyle.Soft,
                shape = ArxIconShape.Circle,
                containerSize = 96.dp,
                iconSize = 44.dp
            )
            StepHeader("Nuevo evento", "Configura los detalles de tu venta porcina.")
        }
        Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)) {
            SectionLabel("Nombre")
            ArxTextField(
                value = name,
                onValueChange = onNameChange,
                label = "Nombre del evento",
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)) {
            SectionLabel("Fecha")
            ArxCard(onClick = { showPicker = true }) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(ArxSpacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)
                ) {
                    ArxIconBadge(icon = ArxIcons.Date, style = ArxIconStyle.Soft)
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Fecha del evento",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            dateMillis?.let(::formatDate) ?: "Selecciona una fecha",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (dateMillis == null) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    ArxIcon(
                        ArxIcons.Forward,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        size = 20.dp
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)) {
            SectionLabel("Cantidad")
            ArxCard {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(ArxSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
                ) {
                    Text(
                        "¿Cuántos animales vas a vender?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    ArxQuantitySelector(
                        value = animalCount.toDouble(),
                        onValueChange = { onAnimalCountChange(it.toInt()) },
                        step = 1.0,
                        unit = "animales",
                        minValue = 1.0
                    )
                }
            }
        }

        Spacer(Modifier.height(ArxSpacing.md)) // respiro antes del botón Continuar

    }
}
@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface
    )
}