package com.example.arrax.ui.screen.lobby.join.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.theme.ArxSpacing

/**
 * Vista del modo QR: cámara (slot) + marco de enfoque + instrucción + linterna.
 * El preview de CameraX y el análisis del QR se conectan en [cameraContent] desde el ViewModel/host.
 */
@Composable
fun JoinQrScannerView(
    torchOn: Boolean,
    errorMessage: String?,
    onTorchToggle: () -> Unit,
    modifier: Modifier = Modifier,
    cameraContent: @Composable () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.lg, Alignment.CenterVertically)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            cameraContent()
            ScanFrame(modifier = Modifier.fillMaxWidth(0.7f).aspectRatio(1f))
        }

        Text(
            text = errorMessage ?: "Apunta la cámara al código QR del negocio.",
            style = MaterialTheme.typography.bodyMedium,
            color = if (errorMessage != null) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = ArxSpacing.md)
        )

        FilledTonalIconToggleButton(
            checked = torchOn,
            onCheckedChange = { onTorchToggle() }
        ) {
            Icon(
                imageVector = if (torchOn) Icons.Filled.FlashlightOn else Icons.Filled.FlashlightOff,
                contentDescription = if (torchOn) "Apagar linterna" else "Encender linterna"
            )
        }
    }
}

/** Marco con esquinas redondeadas; solo dibuja las cuatro esquinas. */
@Composable
private fun ScanFrame(modifier: Modifier = Modifier) {
    val color = MaterialTheme.colorScheme.primary
    Canvas(modifier = modifier) {
        val stroke = 4.dp.toPx()
        val arm = size.minDimension * 0.18f
        val r = 12.dp.toPx()
        val w = size.width
        val h = size.height
        fun corner(x: Float, y: Float, dx: Float, dy: Float) {
            drawLine(color, Offset(x, y + dy * r), Offset(x, y + dy * arm), stroke, StrokeCap.Round)
            drawLine(color, Offset(x + dx * r, y), Offset(x + dx * arm, y), stroke, StrokeCap.Round)
        }
        corner(0f, 0f, 1f, 1f)
        corner(w, 0f, -1f, 1f)
        corner(0f, h, 1f, -1f)
        corner(w, h, -1f, -1f)
    }
}