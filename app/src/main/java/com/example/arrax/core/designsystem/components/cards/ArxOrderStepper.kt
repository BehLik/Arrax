package com.example.arrax.core.designsystem.components.feedback

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxTheme

/**
 * Modelo de datos para definir cada etapa dentro del stepper de pedido.
 */
data class ArxOrderStep(
    val title: String,
    val description: String? = null
)

/**
 * Visualizador horizontal del ciclo de vida y avance del pedido.
 *
 * @param steps Lista de etapas por las que pasa el pedido.
 * @param currentStepIndex Índice base 0 de la etapa activa actual.
 * @param modifier Modificador de diseño Compose.
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
            .padding(vertical = 12.dp, horizontal = 8.dp),
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
                // Nodo e información del paso
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    StepCircleNode(
                        stepNumber = index + 1,
                        isCompleted = isCompleted,
                        isActive = isActive
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = step.title,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isActive || isCompleted) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = when {
                            isActive -> MaterialTheme.colorScheme.primary
                            isCompleted -> MaterialTheme.colorScheme.onSurface
                            else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        },
                        textAlign = TextAlign.Center
                    )

                    if (!step.description.isNullOrEmpty() && isActive) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = step.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Línea conectora horizontal (excepto en el último paso)
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
                            .padding(top = 14.dp) // Alineación con el centro del círculo (28dp / 2)
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
            isActive -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "contentColor"
    )

    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(circleBgColor)
            .then(
                if (isActive) {
                    Modifier.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                } else Modifier
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
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = contentColor
            )
        }
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true)
@Composable
fun ArxOrderStepperPreview() {
    ArxTheme {
        val steps = listOf(
            ArxOrderStep("Generado"),
            ArxOrderStep("En Pesaje", "Estación 2"),
            ArxOrderStep("Empacado"),
            ArxOrderStep("Despachado")
        )

        Column(modifier = Modifier.padding(16.dp)) {
            // Estado: En Pesaje (Paso 2 activo)
            ArxOrderStepper(
                steps = steps,
                currentStepIndex = 1
            )
        }
    }
}