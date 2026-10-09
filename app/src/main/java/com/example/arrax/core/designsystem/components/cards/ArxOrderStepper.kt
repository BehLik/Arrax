package com.example.arrax.core.designsystem.components.feedback

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

// Tamaño del nodo: el conector se alinea con su centro (NodeSize / 2).
private val StepNodeSize = 28.dp

/** Modelo de datos para cada etapa del stepper de pedido. */
data class ArxOrderStep(
    val title: String,
    val description: String? = null
)

/**
 * Visualizador horizontal del avance del pedido.
 *
 * @param steps Etapas por las que pasa el pedido.
 * @param currentStepIndex Índice base 0 de la etapa activa.
 */
@Composable
fun ArxOrderStepper(
    steps: List<ArxOrderStep>,
    currentStepIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = ArxSpacing.sm, horizontal = ArxSpacing.xs),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        steps.forEachIndexed { index, step ->
            val isCompleted = index < currentStepIndex
            val isActive = index == currentStepIndex

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    StepCircleNode(
                        stepNumber = index + 1,
                        isCompleted = isCompleted,
                        isActive = isActive
                    )

                    Spacer(modifier = Modifier.height(ArxSpacing.xs))

                    // El estado se distingue por color y por el nodo (check / anillo / número),
                    // no solo por el peso del texto.
                    Text(
                        text = step.title,
                        style = MaterialTheme.typography.labelMedium,
                        color = when {
                            isActive -> MaterialTheme.colorScheme.primary
                            isCompleted -> MaterialTheme.colorScheme.onSurface
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        textAlign = TextAlign.Center
                    )

                    if (!step.description.isNullOrEmpty() && isActive) {
                        Spacer(modifier = Modifier.height(ArxSpacing.xxs))
                        Text(
                            text = step.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Conector (excepto tras el último paso)
                if (index < steps.size - 1) {
                    val lineColor by animateColorAsState(
                        targetValue = if (index < currentStepIndex) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        },
                        label = "stepperLineColor"
                    )

                    Box(
                        modifier = Modifier
                            .weight(0.8f)
                            .padding(top = StepNodeSize / 2)
                    ) {
                        HorizontalDivider(
                            thickness = 3.dp,
                            color = lineColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepCircleNode(
    stepNumber: Int,
    isCompleted: Boolean,
    isActive: Boolean
) {
    val circleBgColor by animateColorAsState(
        targetValue = when {
            isCompleted -> MaterialTheme.colorScheme.primary
            isActive -> MaterialTheme.colorScheme.primaryContainer
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        label = "circleBgColor"
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            isCompleted -> MaterialTheme.colorScheme.onPrimary
            isActive -> MaterialTheme.colorScheme.onPrimaryContainer
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "contentColor"
    )

    Box(
        modifier = Modifier
            .size(StepNodeSize)
            .clip(CircleShape)
            .background(circleBgColor)
            .then(
                if (isActive) {
                    Modifier.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isCompleted) {
            Icon(
                painter = painterResource(id = ArxIcons.Check),
                contentDescription = "Completado",
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
        } else {
            Text(
                text = stepNumber.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = contentColor
            )
        }
    }
}

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

@Composable
private fun ArxOrderStepperPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val steps = listOf(
                ArxOrderStep("Generado"),
                ArxOrderStep("En pesaje", "Báscula 1"),
                ArxOrderStep("Empacado"),
                ArxOrderStep("Entregado")
            )
            Column(modifier = Modifier.padding(ArxSpacing.md)) {
                ArxOrderStepper(steps = steps, currentStepIndex = 1)
            }
        }
    }
}

@Preview(name = "Stepper – Light", showBackground = false)
@Composable
fun ArxOrderStepperPreviewLight() = ArxOrderStepperPreviewContent()

@Preview(
    name = "Stepper – Dark",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ArxOrderStepperPreviewDark() = ArxOrderStepperPreviewContent()