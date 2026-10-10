package com.example.arrax.core.designsystem.components.voice

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

/**
 * Panel de voz: micrófono + estado + transcripción + comandos + acciones inferiores.
 * [state] es opcional: por defecto se deduce de [isListening] (así las llamadas anteriores no cambian).
 */
@Composable
fun ArxVoicePanel(
    isListening: Boolean,
    statusText: String,
    modifier: Modifier = Modifier,
    recognizedText: String? = null,
    availableCommands: List<String> = emptyList(),
    onMicClick: () -> Unit = {},
    bottomActions: @Composable (() -> Unit)? = null,
    state: ArxVoiceState = if (isListening) ArxVoiceState.Listening else ArxVoiceState.Idle
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(ArxSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ArxMicrophoneButton(state = state, onClick = onMicClick)

        if (state == ArxVoiceState.Listening) {
            ArxVoiceWaveform(isListening = true)
        }

        Spacer(modifier = Modifier.height(ArxSpacing.xl))

        Text(
            text = statusText,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        if (recognizedText != null) {
            Spacer(modifier = Modifier.height(ArxSpacing.xs))
            Text(
                text = "«$recognizedText»",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }

        if (availableCommands.isNotEmpty()) {
            Spacer(modifier = Modifier.height(ArxSpacing.xxl))
            ArxVoiceCommandList(commands = availableCommands)
            Spacer(modifier = Modifier.height(ArxSpacing.xl))
        } else {
            Spacer(modifier = Modifier.height(ArxSpacing.xl))
        }

        if (bottomActions != null) {
            bottomActions()
        }
    }
}

/** Lista de comandos de voz disponibles. */
@Composable
fun ArxVoiceCommandList(
    commands: List<String>,
    modifier: Modifier = Modifier,
    title: String = "Comandos disponibles"
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = ArxSpacing.md)
        )
        Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)) {
            commands.forEach { command ->
                Row(
                    modifier = Modifier.heightIn(min = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = ArxIcons.Check),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(ArxSpacing.xs))
                    Text(
                        text = command,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

@Composable
private fun ArxVoicePanelWeighingContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ArxVoicePanel(
                isListening = true,
                statusText = "Escuchando...",
                recognizedText = "Confirmar peso",
                bottomActions = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)
                    ) {
                        ArxSecondaryButton(text = "Cancelar", onClick = {}, modifier = Modifier.weight(1f))
                        ArxPrimaryButton(text = "Siguiente", onClick = {}, modifier = Modifier.weight(1f))
                    }
                }
            )
        }
    }
}

@Composable
private fun ArxVoicePanelCommandsContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ArxVoicePanel(
                isListening = true,
                statusText = "Escuchando...",
                availableCommands = listOf(
                    "Siguiente pedido",
                    "Repetir",
                    "Corregir peso",
                    "¿Cuánto es el total?",
                    "Cancelar pedido"
                ),
                bottomActions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .clickable { /* Desactivar */ }
                    ) {
                        Icon(
                            painter = painterResource(id = ArxIcons.Mic),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(ArxSpacing.xs))
                        Text(
                            text = "Toca para desactivar",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    }
}

@Preview(name = "Panel pesaje – Light", showBackground = false)
@Composable
fun ArxVoicePanelWeighingPreviewLight() = ArxVoicePanelWeighingContent()

@Preview(name = "Panel pesaje – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxVoicePanelWeighingPreviewDark() = ArxVoicePanelWeighingContent()

@Preview(name = "Panel comandos – Light", showBackground = false)
@Composable
fun ArxVoicePanelCommandsPreviewLight() = ArxVoicePanelCommandsContent()

@Preview(name = "Panel comandos – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxVoicePanelCommandsPreviewDark() = ArxVoicePanelCommandsContent()