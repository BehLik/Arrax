package com.example.arrax.core.designsystem.components.cards

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.components.display.ArxWeightDisplay
import com.example.arrax.core.designsystem.components.feedback.ArxChipStatus
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

/** Variantes de estado de un pedido (statusVariants del JSON). */
enum class ArxOrderCardStatus { Current, Next, Pending, Completed, Error }

/** Chip estándar para cada estado de pedido. Lo comparten la tarjeta y la cola. */
@Composable
internal fun ArxOrderStatusChip(
    status: ArxOrderCardStatus,
    modifier: Modifier = Modifier
) {
    when (status) {
        ArxOrderCardStatus.Current ->
            ArxStatusChip(text = "Actual", status = ArxChipStatus.Info, modifier = modifier)
        ArxOrderCardStatus.Next ->
            ArxStatusChip(text = "Siguiente", isOutlined = true, modifier = modifier)
        ArxOrderCardStatus.Pending ->
            ArxStatusChip(text = "Pendiente", status = ArxChipStatus.Warning, modifier = modifier)
        ArxOrderCardStatus.Completed ->
            ArxStatusChip(text = "Completado", status = ArxChipStatus.Success, modifier = modifier)
        ArxOrderCardStatus.Error ->
            ArxStatusChip(text = "Error", status = ArxChipStatus.Error, modifier = modifier)
    }
}

/**
 * Tarjeta de pedido (corte, folio, cliente, peso, precio/kg y total).
 * Sirve como "pedido actual" (Current) y como "siguiente pedido" (Next).
 * [queueLabel] es opcional, ej. "2 de 5".
 */
@Composable
fun ArxOrderCard(
    cut: String,
    orderId: String,
    client: String,
    weightKg: Double?,
    pricePerKg: Double,
    total: Double?,
    status: ArxOrderCardStatus,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    queueLabel: String? = null
) {
    val isCurrent = status == ArxOrderCardStatus.Current
    val isError = status == ArxOrderCardStatus.Error

    val container by animateColorAsState(
        targetValue = when {
            isCurrent -> MaterialTheme.colorScheme.primaryContainer
            isError -> MaterialTheme.colorScheme.errorContainer
            else -> MaterialTheme.extendedColors.glass
        },
        label = "order_card_container"
    )
    val primaryText = when {
        isCurrent -> MaterialTheme.colorScheme.onPrimaryContainer
        isError -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onSurface
    }
    val secondaryText = when {
        isCurrent -> MaterialTheme.colorScheme.onPrimaryContainer
        isError -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    ArxCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        variant = if (isCurrent || isError) ArxCardVariant.Standard else ArxCardVariant.Glass,
        colors = CardDefaults.cardColors(
            containerColor = container,
            contentColor = primaryText
        )
    ) {
        Column(modifier = Modifier.padding(ArxSpacing.md)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = cut,
                        style = MaterialTheme.typography.headlineMedium,
                        color = primaryText
                    )
                    Text(
                        text = "Pedido #$orderId · $client",
                        style = MaterialTheme.typography.bodyMedium,
                        color = secondaryText
                    )
                    if (queueLabel != null) {
                        Text(
                            text = queueLabel,
                            style = MaterialTheme.typography.labelMedium,
                            color = secondaryText
                        )
                    }
                }
                ArxOrderStatusChip(status = status)
            }

            Spacer(modifier = Modifier.height(ArxSpacing.md))

            ArxWeightDisplay(
                weightKg = weightKg,
                pricePerKg = pricePerKg,
                total = total,
                contentColor = primaryText,
                accentColor = if (isCurrent || isError) primaryText else MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Transición al cambiar de pedido (order_card_transition, 300 ms):
 * la tarjeta nueva entra desplazándose y la anterior sale con fade.
 */
@Composable
fun <T> ArxOrderTransition(
    targetState: T,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    AnimatedContent(
        targetState = targetState,
        modifier = modifier,
        transitionSpec = {
            (slideInHorizontally(tween(300)) { it / 4 } + fadeIn(tween(300))) togetherWith
                    (slideOutHorizontally(tween(300)) { -it / 4 } + fadeOut(tween(300)))
        },
        label = "order_card_transition"
    ) { target ->
        content(target)
    }
}

@Composable
private fun ArxOrderCardPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(ArxSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
            ) {
                ArxOrderCard(
                    cut = "Carne", orderId = "0047", client = "Juan Pérez",
                    weightKg = 2.48, pricePerKg = 100.0, total = 248.0,
                    status = ArxOrderCardStatus.Current
                )
                ArxOrderCard(
                    cut = "Costilla", orderId = "0048", client = "María López",
                    weightKg = 1.76, pricePerKg = 120.0, total = 211.2,
                    status = ArxOrderCardStatus.Next, queueLabel = "2 de 5"
                )
                ArxOrderCard(
                    cut = "Espinazo", orderId = "0049", client = "Carlos Ramírez",
                    weightKg = null, pricePerKg = 100.0, total = null,
                    status = ArxOrderCardStatus.Pending
                )
                ArxOrderCard(
                    cut = "Codillos", orderId = "0050", client = "Ana Torres",
                    weightKg = 2.1, pricePerKg = 90.0, total = 189.0,
                    status = ArxOrderCardStatus.Error
                )
            }
        }
    }
}

@Preview(name = "Tarjeta de pedido – Light", showBackground = false)
@Composable
fun ArxOrderCardPreviewLight() = ArxOrderCardPreviewContent()

@Preview(name = "Tarjeta de pedido – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxOrderCardPreviewDark() = ArxOrderCardPreviewContent()