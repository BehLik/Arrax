package com.example.arrax.core.designsystem.components.voice

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

/**
 * Tarjeta con la respuesta hablada del asistente (texto + botón para repetir el audio).
 * Siempre muestra el texto: la respuesta no depende solo del audio.
 */
@Composable
fun ArxSpokenResponseCard(
    transcript: String,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
    onRetryClick: (() -> Unit)? = null,
    title: String = "Respuesta del asistente"
) {
    ArxCard(
        modifier = modifier.fillMaxWidth(),
        variant = ArxCardVariant.Glass
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArxSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = transcript,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onPlayClick, modifier = Modifier.size(48.dp)) {
                Icon(
                    painter = painterResource(id = ArxIcons.VolumeUp),
                    contentDescription = "Repetir audio",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            if (onRetryClick != null) {
                IconButton(onClick = onRetryClick, modifier = Modifier.size(48.dp)) {
                    Icon(
                        painter = painterResource(id = ArxIcons.Replay),
                        contentDescription = "Reintentar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ArxSpokenResponseCardPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(ArxSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
            ) {
                ArxSpokenResponseCard(
                    transcript = "Perfecto. Pesaremos 2.48 kg de carne para Juan Pérez. El costo es de \$248.00.",
                    onPlayClick = {}
                )
                ArxSpokenResponseCard(
                    transcript = "No te entendí. ¿Puedes repetirlo?",
                    onPlayClick = {},
                    onRetryClick = {}
                )
            }
        }
    }
}

@Preview(name = "Respuesta hablada – Light", showBackground = false)
@Composable
fun ArxSpokenResponseCardPreviewLight() = ArxSpokenResponseCardPreviewContent()

@Preview(name = "Respuesta hablada – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxSpokenResponseCardPreviewDark() = ArxSpokenResponseCardPreviewContent()