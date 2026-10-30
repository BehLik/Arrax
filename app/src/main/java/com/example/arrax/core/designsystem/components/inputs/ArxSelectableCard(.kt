package com.example.arrax.core.designsystem.components.inputs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.extendedColors

@Composable
fun ArxSelectableCard(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val border = if (selected) MaterialTheme.colorScheme.primary
    else MaterialTheme.extendedColors.glassBorder
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.extendedColors.glass,
        border = BorderStroke(if (selected) 2.dp else 1.dp, border)
    ) {
        Column(
            modifier = Modifier.padding(ArxSpacing.sm),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}