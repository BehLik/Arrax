package com.example.arrax.ui.screen.business.home.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.display.color
import com.example.arrax.ui.screen.business.home.HomeSalesSliceUi

@Composable
internal fun SalesDonut(
    slices: List<HomeSalesSliceUi>,
    modifier: Modifier = Modifier
) {
    val colors = slices.map { it.tone.color() }
    val track = MaterialTheme.colorScheme.outlineVariant

    Canvas(modifier = modifier.size(64.dp)) {
        val stroke = 10.dp.toPx()
        val topLeft = Offset(stroke / 2, stroke / 2)
        val arcSize = Size(size.width - stroke, size.height - stroke)

        drawArc(
            color = track, startAngle = 0f, sweepAngle = 360f, useCenter = false,
            topLeft = topLeft, size = arcSize, style = Stroke(stroke)
        )
        var start = -90f
        slices.forEachIndexed { i, slice ->
            val sweep = 360f * slice.fraction
            drawArc(
                color = colors[i], startAngle = start, sweepAngle = sweep, useCenter = false,
                topLeft = topLeft, size = arcSize, style = Stroke(stroke)
            )
            start += sweep
        }
    }
}