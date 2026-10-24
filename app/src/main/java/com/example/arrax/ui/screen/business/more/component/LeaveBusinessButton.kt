package com.example.arrax.ui.screen.business.more.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing

// TODO: migrar a ArxSecondaryButton cuando tenga una variante destructiva (isDestructive).
@Composable
internal fun LeaveBusinessButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val error = MaterialTheme.colorScheme.error
    OutlinedButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, error),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = error),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.sm)
            .heightIn(min = 56.dp)
    ) {
        ArxIcon(icon = ArxIcons.Exit, tint = error, size = 20.dp)
        Text(
            text = "Salir del negocio",
            modifier = Modifier.padding(start = ArxSpacing.xs),
            style = MaterialTheme.typography.labelLarge
        )
    }
}