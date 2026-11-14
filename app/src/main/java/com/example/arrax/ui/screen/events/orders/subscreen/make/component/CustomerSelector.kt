package com.example.arrax.ui.screen.events.orders.subscreen.make.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.display.ArxAvatar
import com.example.arrax.core.designsystem.components.display.ArxListItem
import com.example.arrax.core.designsystem.components.display.arxInitials
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.domain.model.Customer


@Composable
internal fun CustomerSelector(
    selected: Customer?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ArxListItem(
        title = selected?.name ?: "Selecciona un cliente",
        modifier = modifier.fillMaxWidth(),
        subtitle = selected?.phone.orEmpty(),
        leading = {
            if (selected != null) ArxAvatar(initials = arxInitials(selected.name))
            else ArxIcon(icon = ArxIcons.User, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        trailing = {
            Text(
                text = if (selected == null) "Elegir" else "Cambiar",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        },
        onClick = onClick
    )
}