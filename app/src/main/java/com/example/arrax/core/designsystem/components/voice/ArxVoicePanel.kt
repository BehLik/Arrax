package com.example.arrax.core.designsystem.components.voice

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxTheme

@Composable
fun ArxVoicePanel(
    isListening: Boolean,
    statusText: String,
    modifier: Modifier = Modifier,
    recognizedText: String? = null,
    availableCommands: List<String> = emptyList(),
    onMicClick: () -> Unit = {},
    bottomActions: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Animación del Micrófono
        ArxMicRipple(
            isListening = isListening,
            onClick = onMicClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Estado (ej. "Escuchando...")
        Text(
            text = statusText,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        // 3. Texto reconocido o subtítulo (ej. "Confirmar peso")
        if (recognizedText != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "\"$recognizedText\"",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 4. Lista de comandos disponibles (Vista Manos libres completa)
        if (availableCommands.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Comandos disponibles",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(availableCommands) { command ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Check, // Reemplazar con ArxIcons.Bolt o Check pequeño
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = command,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // 5. Acciones inferiores (Botones Siguiente/Cancelar o "Toca para desactivar")
        if (bottomActions != null) {
            bottomActions()
        }
    }
}

@Composable
fun ArxMicRipple(
    isListening: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Configuración de la animación de ondas
    val infiniteTransition = rememberInfiniteTransition(label = "mic_ripple")
    val rippleScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.5f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ripple_scale"
    )
    val rippleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = if (isListening) 0f else 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ripple_alpha"
    )

    Box(
        modifier = modifier.size(140.dp),
        contentAlignment = Alignment.Center
    ) {
        // Onda animada exterior
        if (isListening) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .graphicsLayer {
                        scaleX = rippleScale
                        scaleY = rippleScale
                        alpha = rippleAlpha
                    }
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            )
        }

        // Círculo central (Micrófono)
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(if (isListening) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = ArxIcons.Mic), // Asegúrate de tener ArxIcons.Mic
                contentDescription = "Micrófono",
                tint = if (isListening) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

// ─── PREVIEWS ───

@Preview(showBackground = true, backgroundColor = 0xFF121212, name = "Panel Pesaje (Bottom Sheet)")
@Composable
fun ArxVoicePanelPreview_Weighing() {
    ArxTheme(darkTheme = true) {
        ArxVoicePanel(
            isListening = true,
            statusText = "Escuchando...",
            recognizedText = "Confirmar peso",
            onMicClick = {},
            bottomActions = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ArxSecondaryButton(
                        text = "Cancelar",
                        onClick = {},
                        modifier = Modifier.weight(1f)
                    )
                    ArxPrimaryButton(
                        text = "Siguiente",
                        onClick = {},
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212, name = "Pantalla Manos Libres Completa")
@Composable
fun ArxVoicePanelPreview_FullScreen() {
    ArxTheme(darkTheme = true) {
        ArxVoicePanel(
            isListening = true,
            statusText = "Escuchando...",
            availableCommands = listOf(
                "Siguiente pedido",
                "Repetir pedido",
                "Confirmar cantidad",
                "Marcar entregado",
                "Cancelar / Corregir"
            ),
            onMicClick = {},
            bottomActions = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .clickable { /* Desactivar */ }
                ) {
                    Icon(
                        painter = painterResource(id = ArxIcons.Mic),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
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