package com.example.arrax.core.designsystem.components.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing

@Composable
fun ArxStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)
    ) {
        FilledTonalIconButton(
            onClick = { onValueChange(value - 1) },
            enabled = value > range.first
        ) { Icon(painterResource(ArxIcons.Remove), contentDescription = "Menos") }
        Text(value.toString(), style = MaterialTheme.typography.headlineMedium)
        FilledTonalIconButton(
            onClick = { onValueChange(value + 1) },
            enabled = value < range.last
        ) { Icon(painterResource(ArxIcons.Add), contentDescription = "Más") }
    }
}