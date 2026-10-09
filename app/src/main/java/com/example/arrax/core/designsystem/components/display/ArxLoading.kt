package com.example.arrax.core.designsystem.components.display

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

/** Indicador de carga con mensaje opcional. [isFullScreen] centra el contenido en toda la pantalla. */
@Composable
fun ArxLoading(
    modifier: Modifier = Modifier,
    message: String? = null,
    isFullScreen: Boolean = false
) {
    val rootModifier = if (isFullScreen) {
        modifier.fillMaxSize()
    } else {
        modifier
            .fillMaxWidth()
            .padding(ArxSpacing.xxl)
    }

    Column(
        modifier = rootModifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeWidth = 4.dp
        )

        if (message != null) {
            Spacer(modifier = Modifier.height(ArxSpacing.xl))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

@Composable
private fun ArxLoadingPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column {
                ArxLoading(message = "Sincronizando pedidos...")
                ArxLoading()
            }
        }
    }
}

@Preview(name = "Cargando – Light", showBackground = false)
@Composable
fun ArxLoadingPreviewLight() = ArxLoadingPreviewContent()

@Preview(
    name = "Cargando – Dark",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ArxLoadingPreviewDark() = ArxLoadingPreviewContent()

@Preview(name = "Cargando pantalla completa", showBackground = false)
@Composable
fun ArxLoadingFullScreenPreview() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ArxLoading(isFullScreen = true, message = "Sincronizando pedidos...")
        }
    }
}