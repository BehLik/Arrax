package com.example.arrax.ui.screen.events.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.extendedColors

/** Píldora "Editar" para el slot `actions` del ArxTopBar. */
@Composable
internal fun EventEditPill(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        color = MaterialTheme.extendedColors.glass,
        border = BorderStroke(1.dp, MaterialTheme.extendedColors.glassBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ArxIcon(
                icon = ArxIcons.Settings,
                tint = MaterialTheme.colorScheme.primary,
                size = 16.dp
            )
            Spacer(Modifier.width(ArxSpacing.xs))
            Text(text = "Editar", style = MaterialTheme.typography.labelLarge)
        }
    }
}