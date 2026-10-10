package com.example.arrax.core.designsystem.components.display

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

/**
 * Cronología vertical de etapas (status_timeline del detalle de pedido).
 * - [stageTimes]: hora de cada etapa completada, opcional.
 * - [onStageClick]: si no es null, cada fila completa es tocable (mín. 48dp) para actualizar la etapa;
 *   quien llama decide los permisos.
 */
@Composable
fun ArxOrderStatusTimeline(
    stages: Set<ArxOrderStage>,
    modifier: Modifier = Modifier,
    visibleStages: List<ArxOrderStage> = ArxOrderStage.entries,
    stageTimes: Map<ArxOrderStage, String> = emptyMap(),
    onStageClick: ((ArxOrderStage) -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        visibleStages.forEachIndexed { index, stage ->
            val active = stage in stages
            val isLast = index == visibleStages.lastIndex

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .then(
                        if (onStageClick != null) {
                            Modifier
                                .heightIn(min = 48.dp)
                                .clickable(
                                    role = Role.Button,
                                    onClickLabel = "Actualizar ${stage.label}",
                                    onClick = { onStageClick(stage) }
                                )
                        } else {
                            Modifier
                        }
                    ),
                verticalAlignment = Alignment.Top
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ArxOrderStageIndicator(stage = stage, active = active, size = 28.dp)
                    if (!isLast) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .weight(1f)
                                .background(
                                    if (active) {
                                        arxOrderStageColors(stage).first
                                    } else {
                                        MaterialTheme.colorScheme.outlineVariant
                                    }
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(ArxSpacing.sm))

                Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else ArxSpacing.md)) {
                    Text(
                        text = stage.label,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (active) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                    Text(
                        text = stage.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    val time = stageTimes[stage]
                    if (active && time != null) {
                        Text(
                            text = time,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ArxOrderStatusTimelinePreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ArxOrderStatusTimeline(
                modifier = Modifier.padding(ArxSpacing.md),
                stages = setOf(ArxOrderStage.Registered, ArxOrderStage.Weighed, ArxOrderStage.Delivered),
                stageTimes = mapOf(
                    ArxOrderStage.Registered to "10:24 a. m.",
                    ArxOrderStage.Weighed to "10:31 a. m.",
                    ArxOrderStage.Delivered to "10:40 a. m."
                ),
                onStageClick = {}
            )
        }
    }
}

@Preview(name = "Cronología – Light", showBackground = false)
@Composable
fun ArxOrderStatusTimelinePreviewLight() = ArxOrderStatusTimelinePreviewContent()

@Preview(name = "Cronología – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxOrderStatusTimelinePreviewDark() = ArxOrderStatusTimelinePreviewContent()