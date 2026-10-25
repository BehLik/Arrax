package com.example.arrax.core.designsystem.components.backgrounds

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

/**
 * Raíz de una pantalla: ocupa todo el espacio, es transparente (deja ver el [ArxBackground]
 * del host) y fija el color de contenido por defecto a onSurface.
 */
@Composable
fun ArxScreenSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        content = content
    )
}

// ───────────────────────────── Previews ─────────────────────────────

@Composable
private fun ArxScreenSurfaceSample() {
    ArxBackground(decoration = ArxDecoration.Mountains) {
        ArxScreenSurface {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                Text(
                    text = "ArxScreenSurface",
                    modifier = Modifier.padding(ArxSpacing.xxl),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

@Preview(name = "ScreenSurface - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ArxScreenSurfaceLightPreview() {
    ArxTheme(darkTheme = false) { ArxScreenSurfaceSample() }
}

@Preview(name = "ScreenSurface - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ArxScreenSurfaceDarkPreview() {
    ArxTheme(darkTheme = true) { ArxScreenSurfaceSample() }
}