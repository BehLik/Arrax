package com.example.arrax.core.designsystem.components.cards

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
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
import com.example.arrax.core.designsystem.components.display.arxFormatMoney
import com.example.arrax.core.designsystem.components.display.arxFormatWeight
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

/** Un pedido dentro de la cola. [weightKg] y [total] pueden ser null si aún no se pesa. */
data class ArxQueueOrder(
    val orderId: String,
    val position: Int,
    val cut: String,
    val client: String,
    val weightKg: Double?,
    val total: Double?,
    val status: ArxOrderCardStatus
)

/**
 * Cola de pedidos: encabezado con conteo + filas. El pedido actual se resalta
 * (con animación de color) y el siguiente lleva su insignia.
 * Usa Column (no Lazy) para poder vivir dentro de una pantalla con scroll.
 */
@Composable
fun ArxOrderQueue(
    orders: List<ArxQueueOrder>,
    onOrderClick: (ArxQueueOrder) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Cola de pedidos"
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = ArxSpacing.sm),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            ArxStatusChip(text = "${orders.size} pedidos", isOutlined = true)
        }

        Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
            orders.forEach { order ->
                ArxQueueOrderRow(order = order, onClick = { onOrderClick(order) })
            }
        }
    }
}

@Composable
private fun ArxQueueOrderRow(
    order: ArxQueueOrder,
    onClick: () -> Unit
) {
    val isCurrent = order.status == ArxOrderCardStatus.Current
    val container by animateColorAsState(
        targetValue = if (isCurrent) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.extendedColors.glass
        },
        label = "queue_row_container"
    )
    val textColor = if (isCurrent) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val subtleColor = if (isCurrent) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    ArxCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        variant = if (isCurrent) ArxCardVariant.Standard else ArxCardVariant.Glass,
        colors = CardDefaults.cardColors(containerColor = container, contentColor = textColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArxSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            QueuePositionNode(position = order.position, status = order.status)

            Spacer(modifier = Modifier.width(ArxSpacing.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = order.cut,
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor
                )
                Text(
                    text = "${order.client} · #${order.orderId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = subtleColor
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                if (order.status == ArxOrderCardStatus.Current ||
                    order.status == ArxOrderCardStatus.Next ||
                    order.status == ArxOrderCardStatus.Error
                ) {
                    ArxOrderStatusChip(status = order.status)
                }
                Text(
                    text = order.weightKg?.let(::arxFormatWeight) ?: "—",
                    style = MaterialTheme.typography.bodyMedium,
                    color = subtleColor,
                    textAlign = TextAlign.End
                )
                Text(
                    text = order.total?.let(::arxFormatMoney) ?: "—",
                    style = MaterialTheme.typography.titleSmall,
                    color = textColor,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
private fun QueuePositionNode(position: Int, status: ArxOrderCardStatus) {
    val completed = status == ArxOrderCardStatus.Completed
    val background = when {
        completed || status == ArxOrderCardStatus.Current -> MaterialTheme.colorScheme.primary
        status == ArxOrderCardStatus.Error -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val content = when {
        completed || status == ArxOrderCardStatus.Current -> MaterialTheme.colorScheme.onPrimary
        status == ArxOrderCardStatus.Error -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        if (completed) {
            Icon(
                painter = painterResource(id = ArxIcons.Check),
                contentDescription = "Completado",
                tint = content,
                modifier = Modifier.size(16.dp)
            )
        } else {
            Text(
                text = position.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = content
            )
        }
    }
}

@Composable
private fun ArxOrderQueuePreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ArxOrderQueue(
                modifier = Modifier.padding(ArxSpacing.md),
                orders = listOf(
                    ArxQueueOrder("0047", 1, "Carne", "Juan Pérez", 2.48, 248.0, ArxOrderCardStatus.Current),
                    ArxQueueOrder("0048", 2, "Costilla", "María López", 1.76, 211.2, ArxOrderCardStatus.Next),
                    ArxQueueOrder("0049", 3, "Espinazo", "Carlos Ramírez", 1.32, 132.0, ArxOrderCardStatus.Pending),
                    ArxQueueOrder("0050", 4, "Codillos", "Ana Torres", 2.1, 189.0, ArxOrderCardStatus.Pending),
                    ArxQueueOrder("0046", 5, "Chicharra", "Luis García", 0.85, 76.5, ArxOrderCardStatus.Completed)
                ),
                onOrderClick = {}
            )
        }
    }
}

@Preview(name = "Cola de pedidos – Light", showBackground = false)
@Composable
fun ArxOrderQueuePreviewLight() = ArxOrderQueuePreviewContent()

@Preview(name = "Cola de pedidos – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxOrderQueuePreviewDark() = ArxOrderQueuePreviewContent()