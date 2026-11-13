package com.example.arrax.core.designsystem.components.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard

@Composable
fun ArxOrderSummaryCard(
    title: String,
    totalText: String,
    modifier: Modifier = Modifier,
    caption: String? = null,
) {
    ArxCard(onClick = {}, modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(totalText, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }
            caption?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
        }
    }
}