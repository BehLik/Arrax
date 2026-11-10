package com.example.arrax.ui.screen.events.home.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.arrax.core.common.Money
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.extendedColors
import com.example.arrax.ui.screen.events.home.EventHomeState

@Composable
internal fun EventSummaryCard(
    state: EventHomeState,
    onSeeDetailClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArxCard(variant = ArxCardVariant.Glass, modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(ArxSpacing.md)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ArxIcon(
                        icon = ArxIcons.ChartBar,
                        tint = MaterialTheme.colorScheme.primary,
                        size = 20.dp
                    )
                    Spacer(Modifier.width(ArxSpacing.xs))
                    Text(
                        text = "Resumen del evento",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Surface(
                    onClick = onSeeDetailClick,
                    shape = CircleShape,
                    color = MaterialTheme.extendedColors.glass,
                    border = BorderStroke(1.dp, MaterialTheme.extendedColors.glassBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(start = 12.dp, end = 8.dp, top = 6.dp, bottom = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ver detalle",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(2.dp))
                        ArxIcon(
                            icon = ArxIcons.Forward,
                            size = 14.dp,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(Modifier.height(ArxSpacing.md))

            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SummaryMetric(
                    iconRes = ArxIcons.Piggy,
                    value = "${state.animalCount}",
                    label = "Cerdos",
                    modifier = Modifier.weight(1f)
                )
                SummaryDivider()
                SummaryMetric(
                    iconRes = ArxIcons.Inventory,
                    value = "${state.configuredCutsCount}",
                    label = "Cortes",
                    modifier = Modifier.weight(1f)
                )
                SummaryDivider()
                SummaryMetric(
                    iconRes = ArxIcons.OrderRegistered,
                    value = "${state.ordersCount}",
                    label = "Pedidos",
                    modifier = Modifier.weight(1f)
                )
                SummaryDivider()
                // Más peso y tipografía menor: "$3,240.50" no cabe en 1/4 del ancho con titleLarge
                SummaryMetric(
                    iconRes = ArxIcons.Dollar,
                    value = Money.format(state.totalSalesCents),
                    label = "Ventas totales",
                    valueStyle = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1.4f)
                )
            }
        }
    }
}

@Composable
private fun SummaryDivider() {
    VerticalDivider(
        color = MaterialTheme.extendedColors.glassBorder,
        modifier = Modifier.fillMaxHeight().padding(vertical = 4.dp)
    )
}

@Composable
private fun SummaryMetric(
    @DrawableRes iconRes: Int,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    valueStyle: TextStyle = MaterialTheme.typography.titleLarge
) {
    Column(
        modifier = modifier.padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ArxIcon(
            icon = iconRes,
            tint = MaterialTheme.colorScheme.primary,
            size = 24.dp
        )
        Spacer(Modifier.height(ArxSpacing.xxs))
        Text(
            text = value,
            style = valueStyle,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}