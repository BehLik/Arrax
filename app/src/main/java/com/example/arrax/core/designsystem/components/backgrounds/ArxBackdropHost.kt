package com.example.arrax.core.designsystem.components.backgrounds

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.backgrounds.montain.ArxMountains

@Immutable
data class ArxBackdropSpec(
    val decoration: ArxDecoration = ArxDecoration.None,
    val invertMountains: Boolean = false,
)

/**
 * Las pantallas registran aquí su decoración; gana la última registrada.
 * Al entrar una pantalla nueva se registra después que la anterior (que sigue
 * animando su salida), así que su decoración toma el control de inmediato.
 * Al salir se elimina su registro y vuelve a mandar la que quede.
 */
@Stable
class ArxBackdropState {
    private val requests = mutableStateListOf<Pair<Any, ArxBackdropSpec>>()

    val current: ArxBackdropSpec
        get() = requests.lastOrNull()?.second ?: ArxBackdropSpec()

    fun push(key: Any, spec: ArxBackdropSpec) {
        requests.removeAll { it.first == key }
        requests.add(key to spec)
    }

    fun remove(key: Any) {
        requests.removeAll { it.first == key }
    }
}

/** null = no hay fondo elevado (previews o pantallas fuera del NavHost). */
val LocalArxBackdrop = staticCompositionLocalOf<ArxBackdropState?> { null }

/** Se usa una sola vez, envolviendo el NavHost. */
@Composable
fun ArxBackdropHost(
    state: ArxBackdropState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalArxBackdrop provides state) {
        Box(modifier.fillMaxSize()) {
            ArxBackdrop(spec = state.current)
            content()
        }
    }
}

@Composable
fun ArxBackdrop(spec: ArxBackdropSpec, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ArxDecorationLayer(spec.decoration, spec.invertMountains)
    }
}

/** ÚNICO lugar donde se dibuja cada decoración. Las formas nuevas se agregan aquí. */
@Composable
internal fun BoxScope.ArxDecorationLayer(
    decoration: ArxDecoration,
    invertMountains: Boolean,
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
            // ArxDecoration.Waves -> ArxWaves()   ← futuras formas orgánicas
        }
    }
}