package com.example.arrax.core.designsystem.components.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.inputs.ArxQuantitySelector
import com.example.arrax.core.designsystem.icons.ArxIcons

@Composable
fun ArxOrderItemHorizontalCard(
    title: String,
    pricePerKgText: String,
    totalText: String,
    quantityKg: Double,
    onQuantityChange: (Double) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    imageRes: Int? = null,
) {
    ArxCard(onClick = {}, modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Image(
                painter = painterResource(imageRes ?: ArxIcons.Box),
                contentDescription = null,
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(pricePerKgText, style = MaterialTheme.typography.bodyMedium)
                ArxQuantitySelector(value = quantityKg, onValueChange = onQuantityChange)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(totalText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                TextButton(onClick = onRemove, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text("Quitar")
                }
            }
        }
    }
}