package com.example.arrax.ui.screen.lobby.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant

@Composable
fun WorkspaceCard(
    onCrearNegocioClick: () -> Unit,
    onUnirmeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArxCard(variant = ArxCardVariant.Glass, modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Tus espacios de trabajo",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Gestiona tus negocios, equipos y eventos en un solo lugar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ArxPrimaryButton(
                    text = "+ Crear negocio",
                    onClick = onCrearNegocioClick,
                    hero = true,
                    modifier = Modifier.weight(1f)
                )
                ArxSecondaryButton(
                    text = "Unirme",
                    onClick = onUnirmeClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}