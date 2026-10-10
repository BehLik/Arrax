package com.example.arrax.core.designsystem.components.cards

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.display.ArxAvatar
import com.example.arrax.core.designsystem.components.display.ArxOrderStage
import com.example.arrax.core.designsystem.components.display.ArxOrderStatusIndicators
import com.example.arrax.core.designsystem.components.display.ArxPricePill
import com.example.arrax.core.designsystem.components.display.arxFormatMoney
import com.example.arrax.core.designsystem.components.display.arxInitials
import com.example.arrax.core.designsystem.components.inputs.ArxFilterSortBar
import com.example.arrax.core.designsystem.components.inputs.ArxSegmentedControl
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import java.math.BigDecimal

/**
 * Pedido tal como lo muestra la lista. Las [stages] son independientes entre sí:
 * una combinación como Pagado sin Entregado es válida.
 */
data class ArxOrderListItem(
    val id: String,
    val customer: String,
    val cut: String,
    val quantity: Double,
    val unit: String,
    val time: String,
    val totalMxn: Double,
    val stages: Set<ArxOrderStage>,
    val initials: String = arxInitials(customer)
)

/** 1.0 -> "1", 2.5 -> "2.5", 0.75 -> "0.75". */
internal fun arxFormatQuantity(value: Double): String =
    BigDecimal.valueOf(value).stripTrailingZeros().toPlainString()

/**
 * Tarjeta de pedido para la lista de Pedidos.
 * - Normal: avatar, cliente, "cantidad · corte", total, hora y etapas.
 * - [compact]: versión para listas largas (sin hora, total en texto, etapas pequeñas).
 * Las etapas son informativas aquí; se actualizan desde el detalle, donde caben a 48dp.
 */
@Composable
fun ArxOrderListCard(
    order: ArxOrderListItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    visibleStages: List<ArxOrderStage> = ArxOrderStage.entries
) {
    val summary = "${arxFormatQuantity(order.quantity)} ${order.unit} · ${order.cut}"

    ArxCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        variant = ArxCardVariant.Glass
    ) {
        if (compact) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ArxSpacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ArxAvatar(initials = order.initials, size = 36.dp)
                Spacer(modifier = Modifier.width(ArxSpacing.sm))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order.customer,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = summary,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = arxFormatMoney(order.totalMxn),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.End
                    )
                    ArxOrderStatusIndicators(
                        stages = order.stages,
                        visibleStages = visibleStages,
                        size = 16.dp
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ArxSpacing.md)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ArxAvatar(initials = order.initials)
                    Spacer(modifier = Modifier.width(ArxSpacing.sm))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = order.customer,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = summary,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(ArxSpacing.xs))
                    ArxPricePill(amountMxn = order.totalMxn)
                    Spacer(modifier = Modifier.width(ArxSpacing.xxs))
                    Icon(
                        painter = painterResource(id = ArxIcons.Forward),
                        contentDescription = null, // toda la tarjeta abre el detalle
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(ArxSpacing.xs))

                // Alineada con el texto (ancho del avatar + separación).
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 44.dp + ArxSpacing.sm),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = order.time,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    ArxOrderStatusIndicators(
                        stages = order.stages,
                        visibleStages = visibleStages,
                        size = 22.dp
                    )
                }
            }
        }
    }
}

/** Encabezado de grupo para "Por corte" / "Por cliente": título, cantidad de pedidos y subtotal. */
@Composable
fun ArxOrderGroupHeader(
    title: String,
    orderCount: Int,
    subtotalMxn: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = ArxSpacing.xs),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = if (orderCount == 1) "1 pedido" else "$orderCount pedidos",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = arxFormatMoney(subtotalMxn),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// ───────────────────────── Previews (Light / Dark) ─────────────────────────

private val sampleOrders = listOf(
    ArxOrderListItem(
        "order_001", "Diana López", "Morcilla", 1.0, "kg", "10:24 a. m.", 130.0,
        setOf(ArxOrderStage.Registered, ArxOrderStage.Weighed, ArxOrderStage.Delivered)
    ),
    ArxOrderListItem(
        "order_002", "Marta García", "Morcilla", 22.0, "g", "10:37 a. m.", 28.6,
        setOf(ArxOrderStage.Registered, ArxOrderStage.Weighed, ArxOrderStage.Delivered)
    ),
    ArxOrderListItem(
        "order_003", "Carlos Ramírez", "Carne", 2.5, "kg", "10:52 a. m.", 325.0,
        setOf(ArxOrderStage.Registered, ArxOrderStage.Delivered)
    ),
    ArxOrderListItem(
        "order_004", "Ana Torres", "Costilla", 1.2, "kg", "11:03 a. m.", 156.0,
        setOf(ArxOrderStage.Registered, ArxOrderStage.Weighed, ArxOrderStage.Paid)
    )
)

@Composable
private fun ArxOrderListPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            var mode by remember { mutableIntStateOf(0) }
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = ArxSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
            ) {
                ArxSegmentedControl(
                    options = listOf("Por corte", "Por cliente"),
                    selectedIndex = mode,
                    onOptionSelected = { mode = it }
                )
                ArxFilterSortBar(label = "Todos los cortes", onFilterClick = {}, onSortClick = {})
                ArxOrderGroupHeader(title = "Morcilla", orderCount = 2, subtotalMxn = 158.6)
                sampleOrders.take(2).forEach { ArxOrderListCard(order = it, onClick = {}) }
                ArxOrderGroupHeader(title = "Otros cortes", orderCount = 2, subtotalMxn = 481.0)
                sampleOrders.drop(2).forEach { ArxOrderListCard(order = it, onClick = {}, compact = true) }
            }
        }
    }
}

@Preview(name = "Lista de pedidos – Light", showBackground = false)
@Composable
fun ArxOrderListPreviewLight() = ArxOrderListPreviewContent()

@Preview(name = "Lista de pedidos – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxOrderListPreviewDark() = ArxOrderListPreviewContent()