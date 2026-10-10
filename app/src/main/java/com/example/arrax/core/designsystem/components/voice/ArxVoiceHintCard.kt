package com.example.arrax.core.designsystem.components.voice

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

/** Sugerencia de comando de voz (ej. «Siguiente pedido»). */
@Composable
fun ArxVoiceHintCard(
    phrase: String,
    modifier: Modifier = Modifier,
    label: String = "Prueba decir"
) {
    ArxCard(
        modifier = modifier.fillMaxWidth(),
        variant = ArxCardVariant.Glass
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArxSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = ArxIcons.Mic),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(ArxSpacing.sm))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "«$phrase»",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun ArxVoiceHintCardPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ArxVoiceHintCard(
                phrase = "Empecemos con el pesaje de carne",
                modifier = Modifier.padding(ArxSpacing.md)
            )
        }
    }
}

@Preview(name = "Sugerencia de voz – Light", showBackground = false)
@Composable
fun ArxVoiceHintCardPreviewLight() = ArxVoiceHintCardPreviewContent()

@Preview(name = "Sugerencia de voz – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxVoiceHintCardPreviewDark() = ArxVoiceHintCardPreviewContent()