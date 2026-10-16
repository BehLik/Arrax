package com.example.arrax.core.designsystem.components.feedback

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

/**
 * Estado de conectividad/sincronización YA resuelto para la UI.
 * Este componente solo lo dibuja: quién lo calcula (ConnectivityManager, Firestore, etc.)
 * es cosa del ViewModel, que lo expone, por ejemplo, así:
 *
 *     val status = when {
 *         !isOnline -> ArxConnectionStatus.Offline
 *         syncFailed -> ArxConnectionStatus.SyncError
 *         hasPendingWrites -> ArxConnectionStatus.Syncing
 *         else -> ArxConnectionStatus.Online
 *     }
 */
enum class ArxConnectionStatus(val label: String) {
    Online("Conectado"),
    Syncing("Sincronizando"),
    Offline("Sin conexión"),
    SyncError("Error de sincronización")
}

/**
 * Indicador discreto de conectividad. Cada estado tiene icono y descripción propios,
 * así que el estado no depende solo del color. Con [showLabel] muestra también el texto.
 */
@Composable
fun ArxConnectionIndicator(
    status: ArxConnectionStatus,
    modifier: Modifier = Modifier,
    showLabel: Boolean = false
) {
    val iconRes = when (status) {
        ArxConnectionStatus.Online -> ArxIcons.Cloud
        ArxConnectionStatus.Syncing -> ArxIcons.Sync
        ArxConnectionStatus.Offline -> ArxIcons.WifiOff
        ArxConnectionStatus.SyncError -> ArxIcons.SyncOff
    }
    val tint = when (status) {
        ArxConnectionStatus.Online -> MaterialTheme.extendedColors.success.color
        ArxConnectionStatus.Syncing -> MaterialTheme.extendedColors.info.color
        ArxConnectionStatus.Offline -> MaterialTheme.extendedColors.warning.color
        ArxConnectionStatus.SyncError -> MaterialTheme.colorScheme.error
    }
    val rotation = rememberSyncRotation(active = status == ArxConnectionStatus.Syncing)

    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.extendedColors.glass)
            .border(1.dp, MaterialTheme.extendedColors.glassBorder, CircleShape)
            .padding(horizontal = ArxSpacing.xs, vertical = ArxSpacing.xs)
            .semantics(mergeDescendants = true) { contentDescription = status.label },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .size(18.dp)
                .graphicsLayer { rotationZ = rotation }
        )
        if (showLabel) {
            Spacer(modifier = Modifier.width(ArxSpacing.xxs))
            Text(
                text = status.label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// Solo gira mientras sincroniza; en cualquier otro estado no hay animación corriendo.
@Composable
private fun rememberSyncRotation(active: Boolean): Float {
    if (!active) return 0f
    val transition = rememberInfiniteTransition(label = "sync_rotation")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    return rotation
}

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

@Composable
private fun ArxConnectionIndicatorPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier.padding(ArxSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
            ) {
                ArxConnectionStatus.entries.forEach { status ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
                    ) {
                        ArxConnectionIndicator(status = status)
                        ArxConnectionIndicator(status = status, showLabel = true)
                    }
                }
            }
        }
    }
}

@Preview(name = "Conectividad – Light", showBackground = false)
@Composable
fun ArxConnectionIndicatorPreviewLight() = ArxConnectionIndicatorPreviewContent()

@Preview(name = "Conectividad – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxConnectionIndicatorPreviewDark() = ArxConnectionIndicatorPreviewContent()