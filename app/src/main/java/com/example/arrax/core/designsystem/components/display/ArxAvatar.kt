package com.example.arrax.core.designsystem.components.display

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

/** Iniciales de un nombre: "Diana López" -> "DL", "Ana" -> "A". Máximo 2 letras. */
internal fun arxInitials(name: String): String =
    name.trim()
        .split(Regex("\\s+"))
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

/** Avatar circular con iniciales. Decorativo: el nombre ya lo lee el texto cercano. */
@Composable
fun ArxAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(containerColor)
            .clearAndSetSemantics { },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials.take(2).uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = contentColor
        )
    }
}

@Composable
private fun ArxAvatarPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Row(
                modifier = Modifier.padding(ArxSpacing.md),
                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ArxAvatar(initials = arxInitials("Diana López"))
                ArxAvatar(initials = arxInitials("Carlos Ramírez"), size = 36.dp)
                ArxAvatar(initials = arxInitials("Ana"), size = 56.dp)
            }
        }
    }
}

@Preview(name = "Avatar – Light", showBackground = false)
@Composable
fun ArxAvatarPreviewLight() = ArxAvatarPreviewContent()

@Preview(name = "Avatar – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxAvatarPreviewDark() = ArxAvatarPreviewContent()