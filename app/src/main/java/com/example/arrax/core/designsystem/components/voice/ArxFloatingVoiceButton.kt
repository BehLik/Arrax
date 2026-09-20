package com.example.arrax.core.designsystem.components.voice

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxTheme

@Composable
fun ArxFloatingVoiceButton(
    isListening: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animación de color cuando cambia de estado
    val containerColor by animateColorAsState(
        targetValue = if (isListening) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
        label = "fab_container_color"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isListening) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer,
        label = "fab_content_color"
    )

    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(64.dp),
        shape = CircleShape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = if (isListening) 8.dp else 4.dp
        )
    ) {
        Icon(
            // Puedes cambiar el ícono a uno de "Stop" si está escuchando
            painter = painterResource(id = ArxIcons.Mic),
            contentDescription = if (isListening) "Detener voz" else "Activar voz",
            modifier = Modifier.size(28.dp)
        )
    }
}

@Preview(showBackground = true, name = "Botón Flotante de Voz")
@Composable
fun ArxFloatingVoiceButtonPreview() {
    ArxTheme {
        ArxFloatingVoiceButton(
            isListening = false,
            onClick = {}
        )
    }
}