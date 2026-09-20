package com.example.arrax.core.designsystem.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arrax.core.designsystem.theme.ArxTheme

@Composable
fun ArxBadge(
    count: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.error,
    contentColor: Color = MaterialTheme.colorScheme.onError,
    maxCount: Int = 99
) {
    if (count > 0) {
        val displayCount = if (count > maxCount) "$maxCount+" else count.toString()

        Box(
            modifier = modifier
                .sizeIn(minWidth = 18.dp, minHeight = 18.dp)
                .clip(CircleShape)
                .background(containerColor)
                .padding(horizontal = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayCount,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = contentColor,
                maxLines = 1
            )
        }
    }
}

// Sobrecarga para crear un "Dot" (Punto) de notificación sin números
@Composable
fun ArxBadgeDot(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.error
) {
    Box(
        modifier = modifier
            .sizeIn(minWidth = 8.dp, minHeight = 8.dp)
            .clip(CircleShape)
            .background(containerColor)
    )
}

@Preview(showBackground = true)
@Composable
fun ArxBadgePreview() {
    ArxTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ArxBadge(count = 5)
            ArxBadge(count = 125, maxCount = 99) // Mostrará "99+"
            ArxBadgeDot() // Solo el punto de notificación
        }
    }
}