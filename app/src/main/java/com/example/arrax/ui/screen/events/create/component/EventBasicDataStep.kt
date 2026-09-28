package com.example.arrax.ui.screen.events.create.component

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.components.inputs.ArxQuantitySelector
import com.example.arrax.core.designsystem.components.inputs.ArxTextField
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// FIX: ArxQuantitySelector trabaja con Double, no Int. El ViewModel/State siguen
// en Int (pigCount es Int en Event) - la conversion se hace aqui, en el borde de UI.
@Composable
fun EventBasicDataStep(
    name: String,
    dateMillis: Long?,
    pigCount: Int,
    canContinue: Boolean,
    onNameChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onPigCountChange: (Int) -> Unit,
    onContinue: () -> Unit
) {
    val context = LocalContext.current
    val calendar = remember(dateMillis) {
        Calendar.getInstance().apply { dateMillis?.let { timeInMillis = it } }
    }
    val dateLabel = dateMillis?.let {
        SimpleDateFormat("dd/MM/yyyy", Locale("es", "MX")).format(it)
    } ?: "Selecciona una fecha"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ArxTextField(
            value = name,
            onValueChange = onNameChange,
            label = "Nombre del evento"
        )

        ArxPrimaryButton(
            text = dateLabel,
            onClick = {
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val picked = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth, 0, 0, 0)
                        }
                        onDateChange(picked.timeInMillis)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        )

        Text("Cantidad de cerdos")
        ArxQuantitySelector(
            value = pigCount.toDouble(),
            minValue = 1.0,
            onValueChange = { newValue -> onPigCountChange(newValue.toInt()) }
        )

        ArxSecondaryButton(
            text = "Continuar",
            enabled = canContinue,
            onClick = onContinue
        )
    }
}
