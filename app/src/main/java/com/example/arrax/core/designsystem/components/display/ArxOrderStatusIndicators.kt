package com.example.arrax.core.designsystem.components.display

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

/**
 * Etapas de un pedido (order_statuses del JSON). Son INDEPENDIENTES: un pedido puede tener
 * cualquier combinación (ej. "Pagado" sin "Entregado"). Si tu negocio las quiere secuenciales,
 * esa regla vive en el ViewModel, no aquí.
 */
enum class ArxOrderStage(
    val label: String,
    val description: String,
    val iconRes: Int
) {
    Registered("Registrado", "Pedido creado en el sistema.", ArxIcons.Check),
    Weighed("Pesado", "Producto pesado y confirmado.", ArxIcons.DoneAll),
    Delivered("Entregado", "Producto entregado al cliente.", ArxIcons.DoneAll),
    Paid("Pagado", "Pago recibido completo o parcial.", ArxIcons.PaymentDone)
}

/**
 * Colores (relleno, contenido) de cada etapa, siempre desde el tema.
 * Es el único lugar donde se asigna: si quieres tonos propios para Entregado/Pagado
 * (el JSON usa azul índigo y morado), añade esas familias a extendedColors y cámbialo aquí.
 */
@Composable
fun arxOrderStageColors(stage: ArxOrderStage): Pair<Color, Color> = when (stage) {
    ArxOrderStage.Registered -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
    ArxOrderStage.Weighed -> MaterialTheme.extendedColors.info.let { it.color to it.onColor }
    ArxOrderStage.Delivered -> MaterialTheme.colorScheme.tertiary to MaterialTheme.colorScheme.onTertiary
    ArxOrderStage.Paid -> MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.onSecondary
}

/**
 * Indicador circular de una etapa. Activo = relleno + check; inactivo = apagado y sin icono,
 * así el estado no depende solo del color. Siempre lleva descripción para TalkBack.
 * Con [onClick] el área táctil crece a 48dp.
 */
@Composable
fun ArxOrderStageIndicator(
    stage: ArxOrderStage,
    active: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    onClick: (() -> Unit)? = null
) {
    val (stageColor, onStageColor) = arxOrderStageColors(stage)
    val background by animateColorAsState(
        targetValue = if (active) stageColor else MaterialTheme.colorScheme.surfaceVariant,
        label = "stage_background"
    )
    val description = "${stage.label}: ${if (active) "sí" else "pendiente"}"

    val clickable = if (onClick != null) {
        Modifier
            .minimumInteractiveComponentSize()
            .clickable(role = Role.Button, onClickLabel = "Actualizar ${stage.label}", onClick = onClick)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .then(clickable)
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(background)
                .then(
                    if (active) {
                        Modifier
                    } else {
                        Modifier.border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (active) {
                Icon(
                    painter = painterResource(id = stage.iconRes),
                    contentDescription = null,
                    tint = onStageColor,
                    modifier = Modifier.size(size * 0.6f)
                )
            }
        }
    }
}

/**
 * Fila de indicadores de etapa. [visibleStages] permite que cada flujo muestre solo las etapas
 * que le aplican (ej. un pedido en mostrador no tiene "Entregado").
 * Si pasas [onStageClick], cada círculo es tocable (48dp): úsalo donde haya espacio, como el detalle.
 */
@Composable
fun ArxOrderStatusIndicators(
    stages: Set<ArxOrderStage>,
    modifier: Modifier = Modifier,
    visibleStages: List<ArxOrderStage> = ArxOrderStage.entries,
    size: Dp = 24.dp,
    onStageClick: ((ArxOrderStage) -> Unit)? = null
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xxs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        visibleStages.forEach { stage ->
            ArxOrderStageIndicator(
                stage = stage,
                active = stage in stages,
                size = size,
                onClick = onStageClick?.let { handler -> { handler(stage) } }
            )
        }
    }
}

@Composable
private fun ArxOrderStatusIndicatorsPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(ArxSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
            ) {
                ArxOrderStatusIndicators(stages = setOf(ArxOrderStage.Registered))
                ArxOrderStatusIndicators(stages = setOf(ArxOrderStage.Registered, ArxOrderStage.Weighed))
                ArxOrderStatusIndicators(stages = setOf(ArxOrderStage.Weighed, ArxOrderStage.Delivered))
                ArxOrderStatusIndicators(stages = ArxOrderStage.entries.toSet())
                ArxOrderStatusIndicators(
                    stages = setOf(ArxOrderStage.Registered, ArxOrderStage.Weighed),
                    visibleStages = listOf(ArxOrderStage.Registered, ArxOrderStage.Weighed, ArxOrderStage.Paid)
                )
            }
        }
    }
}

@Preview(name = "Etapas – Light", showBackground = false)
@Composable
fun ArxOrderStatusIndicatorsPreviewLight() = ArxOrderStatusIndicatorsPreviewContent()

@Preview(name = "Etapas – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxOrderStatusIndicatorsPreviewDark() = ArxOrderStatusIndicatorsPreviewContent()