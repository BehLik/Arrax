package com.example.arrax.core.designsystem.components.backgrounds

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.components.backgrounds.montain.ArxMountains
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme



/**
 * Fondo de pantalla de Arrax: color sólido del tema + decoración opcional abajo.
 *
 * @param decoration     forma decorativa; [ArxDecoration.None] = solo color.
 * @param invertMountains pon true si en tu SVG lo cercano es blanco y lo lejano negro.
 *
 * Usa MaterialTheme.colorScheme -> compatible con Light/Dark.
 */
@Composable
fun ArxBackground(
    modifier: Modifier = Modifier,
    decoration: ArxDecoration = ArxDecoration.None,
    invertMountains: Boolean = false,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Crossfade(
            targetState = decoration,
            animationSpec = tween(400),
            modifier = Modifier.align(Alignment.BottomCenter),
            label = "arxDecoration",
        ) { current ->
            when (current) {
                ArxDecoration.None -> Unit
                ArxDecoration.Mountains -> ArxMountains(invert = invertMountains)
            }
        }
        content()
    }
}

// ───────────────────────────── Previews ─────────────────────────────

@Preview(name = "Light - Mountains", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ArxBackgroundPreviewLightOn() {
    ArxTheme {
        ArxBackground(decoration = ArxDecoration.Mountains) {
            Text(
                text = "Arx Background (Light)",
                modifier = Modifier.align(Alignment.TopCenter).padding(ArxSpacing.giant),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Preview(name = "Light - None", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ArxBackgroundPreviewLightOff() {
    ArxTheme {
        ArxBackground {
            Text(
                text = "Solo color",
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Preview(name = "Dark - Mountains", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ArxBackgroundPreviewDark() {
    ArxTheme(darkTheme = true) {
        ArxBackground(decoration = ArxDecoration.Mountains) {
            Text(
                text = "Arx Background (Dark)",
                modifier = Modifier.align(Alignment.TopCenter).padding(ArxSpacing.giant),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}