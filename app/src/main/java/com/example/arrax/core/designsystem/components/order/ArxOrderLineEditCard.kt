package com.example.arrax.core.designsystem.components.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.buttons.ArxTextButton
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCatalogImage
import com.example.arrax.core.designsystem.components.cards.ArxCatalogImageView
import com.example.arrax.core.designsystem.components.inputs.ArxQuantitySelector
import com.example.arrax.core.designsystem.icons.ArxIcons
import java.util.Locale

@Composable
fun ArxOrderLineEditCard(
    title: String,
    pricePerKgText: String,
    totalText: String,
    isMoneyMode: Boolean,
    grams: Int,
    amountCents: Long,
    equivalentText: String?,
    onModeChange: (isMoney: Boolean) -> Unit,
    onGramsChange: (Int) -> Unit,
    onAmountCentsChange: (Long) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    image: ArxCatalogImage = ArxCatalogImage.None,
    focusRequester: FocusRequester = remember { FocusRequester() },
    onImeNext: () -> Unit = {},
) {
    ArxCard(onClick = {}, modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ArxCatalogImageView(image = image, title = title, modifier = Modifier.size(64.dp))
                Column(Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(pricePerKgText, style = MaterialTheme.typography.bodyMedium)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(totalText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    ArxTextButton(
                        text = "Quitar",
                        onClick = onRemove,
                        isDestructive = true
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = !isMoneyMode,
                    onClick = { onModeChange(false); runCatching { focusRequester.requestFocus() } },
                    label = { Text("Kilos") },
                )
                FilterChip(
                    selected = isMoneyMode,
                    onClick = { onModeChange(true); runCatching { focusRequester.requestFocus() } },
                    label = { Text("Pesos") },
                )
            }
            LineQuantityInput(
                isMoney = isMoneyMode, grams = grams, amountCents = amountCents,
                equivalentText = equivalentText,
                onGramsChange = onGramsChange, onAmountCentsChange = onAmountCentsChange,
                focusRequester = focusRequester, onImeNext = onImeNext,
            )
        }
    }
}

@Composable
private fun LineQuantityInput(
    isMoney: Boolean,
    grams: Int,
    amountCents: Long,
    equivalentText: String?,
    onGramsChange: (Int) -> Unit,
    onAmountCentsChange: (Long) -> Unit,
    focusRequester: FocusRequester,
    onImeNext: () -> Unit,
) {
    val external = if (isMoney) amountCents else grams.toLong()
    var field by remember { mutableStateOf(TextFieldValue(formatUnits(isMoney, external))) }
    LaunchedEffect(isMoney, external) {
        if (parseUnits(isMoney, field.text) != external) {
            val t = formatUnits(isMoney, external)
            field = TextFieldValue(t, TextRange(t.length))
        }
    }
    fun set(units: Long) = if (isMoney) onAmountCentsChange(units) else onGramsChange(units.toInt())

    val quick: List<Pair<String, Long>> =
        if (isMoney) listOf("$50" to 5_000L, "$100" to 10_000L, "$200" to 20_000L, "$500" to 50_000L)
        else listOf("¼" to 250L, "½" to 500L, "¾" to 750L, "1 kg" to 1_000L, "2 kg" to 2_000L)

    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        quick.forEach { (label, units) ->
            FilterChip(selected = external == units, onClick = { set(units) }, label = { Text(label) })
        }
    }
    OutlinedTextField(
        value = field,
        onValueChange = { v ->
            if (v.text.matches(Regex("[0-9]*[.,]?[0-9]*"))) {
                field = v
                set(if (v.text.isBlank()) 0L else parseUnits(isMoney, v.text) ?: 0L)
            }
        },
        label = { Text(if (isMoney) "Monto" else "Cantidad") },
        prefix = if (isMoney) ({ Text("$") }) else null,
        suffix = if (!isMoney) ({ Text("kg") }) else null,
        supportingText = equivalentText?.let { { Text(it) } },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { onImeNext() }),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { if (it.isFocused) field = field.copy(selection = TextRange(0, field.text.length)) },
    )
}

private fun parseUnits(isMoney: Boolean, raw: String): Long? =
    raw.replace(',', '.').toDoubleOrNull()?.let { Math.round(it * if (isMoney) 100 else 1000) }

private fun formatUnits(isMoney: Boolean, units: Long): String {
    if (units == 0L) return ""
    val v = units / if (isMoney) 100.0 else 1000.0
    return String.format(Locale.US, if (isMoney) "%.2f" else "%.3f", v).trimEnd('0').trimEnd('.')
}