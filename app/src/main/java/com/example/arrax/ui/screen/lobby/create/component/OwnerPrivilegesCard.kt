package com.example.arrax.ui.screen.lobby.create.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.extendedColors

@Composable
 fun OwnerPrivilegesCard() {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.extendedColors.glass,
        border = BorderStroke(ArxSpacing.xxs, MaterialTheme.extendedColors.glassBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(ArxSpacing.md),
            verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
        ) {
            Text(
                text = "Serás el propietario",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            PrivilegeRow(ArxIcons.Admin, "Administras todo el negocio")
            PrivilegeRow(ArxIcons.GroupAdd, "Invitas operadores y los ascendes a administradores")
            PrivilegeRow(ArxIcons.Event, "Creas eventos y eliges sus cortes y precios")
        }
    }
}