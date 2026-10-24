package com.example.arrax.ui.screen.business.home.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.extendedColors

@Composable
internal fun QuickActionsSection(
    onCreateEventClick: () -> Unit,
    onCustomersClick: () -> Unit,
    onTeamClick: () -> Unit,
    onCutsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
    ) {
        Text("Acciones rápidas", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
            QuickActionTile("Crear evento", ArxIcons.Event, true, onCreateEventClick, Modifier.weight(1f))
            QuickActionTile("Clientes", ArxIcons.User, false, onCustomersClick, Modifier.weight(1f))
            QuickActionTile("Equipo", ArxIcons.TeamOutline, false, onTeamClick, Modifier.weight(1f))
            QuickActionTile("Cortes", ArxIcons.Box, false, onCutsClick, Modifier.weight(1f))
        }
    }
}

/**
 * Versión compacta: cuatro tiles en una fila a 360dp no caben con ArxFieldActionButton
 * (ícono de 36dp, titleMedium y padding xl).
 */
@Composable
private fun QuickActionTile(
    label: String,
    @DrawableRes icon: Int,
    highlighted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val glass = MaterialTheme.extendedColors
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = if (highlighted) colors.primary else glass.glass,
        contentColor = if (highlighted) colors.onPrimary else colors.onSurface,
        border = if (highlighted) null else BorderStroke(1.dp, glass.glassBorder)
    ) {
        Column(
            modifier = Modifier
                .clickable(role = Role.Button, onClick = onClick)
                .heightIn(min = 80.dp)
                .padding(ArxSpacing.xs),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ArxIcon(icon = icon, tint = if (highlighted) colors.onPrimary else colors.primary)
            Spacer(Modifier.height(ArxSpacing.xxs))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}