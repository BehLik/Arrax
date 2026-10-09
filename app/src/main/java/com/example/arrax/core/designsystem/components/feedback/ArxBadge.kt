package com.example.arrax.core.designsystem.components.feedback

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

/** Insignia numérica. No se muestra si [count] es 0. Con más de [maxCount] muestra "99+". */
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
                .sizeIn(minWidth = 20.dp, minHeight = 20.dp)
                .clip(CircleShape)
                .background(containerColor)
                .padding(horizontal = ArxSpacing.xxs),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayCount,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor,
                maxLines = 1
            )
        }
    }
}

/** Punto de notificación sin número. */
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

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

@Composable
private fun ArxBadgePreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Row(
                modifier = Modifier.padding(ArxSpacing.md),
                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ArxBadge(count = 5)
                ArxBadge(count = 125, maxCount = 99) // "99+"
                ArxBadgeDot()
            }
        }
    }
}

@Preview(name = "Badge – Light", showBackground = false)
@Composable
fun ArxBadgePreviewLight() = ArxBadgePreviewContent()

@Preview(
    name = "Badge – Dark",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ArxBadgePreviewDark() = ArxBadgePreviewContent()