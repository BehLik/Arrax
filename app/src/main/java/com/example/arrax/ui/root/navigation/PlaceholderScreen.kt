package com.example.arrax.ui.root.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class PlaceholderAction(val label: String, val onClick: () -> Unit)

@Composable
fun PlaceholderScreen(
    title: String,
    onBack: () -> Unit,
    actions: List<PlaceholderAction> = emptyList()
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().systemBarsPadding().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Text(title, style = MaterialTheme.typography.headlineSmall)
            Text("Pantalla en construcción", style = MaterialTheme.typography.bodyMedium)
            actions.forEach { action ->
                OutlinedButton(onClick = action.onClick) { Text(action.label) }
            }
            TextButton(onClick = onBack) { Text("Volver") }
        }
    }
}