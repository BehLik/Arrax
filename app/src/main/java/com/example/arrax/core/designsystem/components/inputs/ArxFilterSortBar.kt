package com.example.arrax.core.designsystem.components.inputs

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

/**
 * Barra de filtro + orden (filter_row del JSON): a la izquierda el filtro actual
 * (ej. "Todos los cortes") y a la derecha el botón para cambiar el orden.
 */
@Composable
fun ArxFilterSortBar(
    label: String,
    onFilterClick: () -> Unit,
    onSortClick: () -> Unit,
    modifier: Modifier = Modifier,
    sortContentDescription: String = "Cambiar orden"
) {
    val shape = MaterialTheme.shapes.small

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.extendedColors.glass)
            .border(1.dp, MaterialTheme.extendedColors.glassBorder, shape),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 48.dp)
                .clickable(role = Role.Button, onClickLabel = "Cambiar filtro", onClick = onFilterClick)
                .padding(horizontal = ArxSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = ArxIcons.Filter),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(ArxSpacing.xs))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(onClick = onSortClick, modifier = Modifier.size(48.dp)) {
            Icon(
                painter = painterResource(id = ArxIcons.Sort),
                contentDescription = sortContentDescription,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ArxFilterSortBarPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ArxFilterSortBar(
                label = "Todos los cortes",
                onFilterClick = {},
                onSortClick = {},
                modifier = Modifier.padding(ArxSpacing.md)
            )
        }
    }
}

@Preview(name = "Filtro y orden – Light", showBackground = false)
@Composable
fun ArxFilterSortBarPreviewLight() = ArxFilterSortBarPreviewContent()

@Preview(name = "Filtro y orden – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxFilterSortBarPreviewDark() = ArxFilterSortBarPreviewContent()