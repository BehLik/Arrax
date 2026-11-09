package com.example.arrax.ui.screen.events.home.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.arrax.core.common.Money
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.components.feedback.ArxChipStatus
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIconBadge
import com.example.arrax.core.designsystem.icons.ArxIconShape
import com.example.arrax.core.designsystem.icons.ArxIconStyle
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.extendedColors
import com.example.arrax.ui.screen.events.home.RecentOrderStatus
import com.example.arrax.ui.screen.events.home.RecentOrderUi

private data class OrderStatusStyle(
    val label: String,
    val chipStatus: ArxChipStatus,
    @DrawableRes val iconRes: Int
)

private fun RecentOrderStatus.style(): OrderStatusStyle = when (this) {
    RecentOrderStatus.DELIVERED ->
        OrderStatusStyle("Entregado", ArxChipStatus.Success, ArxIcons.Done)
    RecentOrderStatus.IN_TRANSIT ->
        OrderStatusStyle("En camino", ArxChipStatus.Info, ArxIcons.Delivery)
    RecentOrderStatus.PENDING ->
        OrderStatusStyle("Pendiente", ArxChipStatus.Warning, ArxIcons.Pending)
}

@Composable
internal fun RecentOrderRowCard(
    order: RecentOrderUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusStyle = order.status.style()

    ArxCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        variant = ArxCardVariant.Glass
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(ArxSpacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1) Cliente y dirección
            Row(
                modifier = Modifier.weight(1.3f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ArxIconBadge(
                    icon = ArxIcons.User,
                    style = ArxIconStyle.Tonal,
                    shape = ArxIconShape.Circle,
                    containerSize = 40.dp,
                    iconSize = 20.dp
                )
                Spacer(Modifier.width(ArxSpacing.xs))
                Column {
                    Text(
                        text = order.customerName,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ArxIcon(
                            icon = ArxIcons.Location,
                            size = 12.dp,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = order.address,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            RowDivider()

            // 2) Corte y peso
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(order.cutImageRes),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(ArxSpacing.xs))
                Column {
                    Text(
                        text = order.cutName,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = order.weightLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }

            RowDivider()

            // 3) Total
            Text(
                text = Money.format(order.totalCents),
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1
            )

            RowDivider()

            // 4) Estado, hora y chevron
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    ArxStatusChip(
                        text = statusStyle.label,
                        status = statusStyle.chipStatus,
                        iconRes = statusStyle.iconRes
                    )
                    Spacer(Modifier.height(ArxSpacing.xxs))
                    Text(
                        text = order.timeLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
                Spacer(Modifier.width(ArxSpacing.xxs))
                ArxIcon(
                    icon = ArxIcons.Forward,
                    size = 16.dp,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun RowDivider() {
    VerticalDivider(
        color = MaterialTheme.extendedColors.glassBorder,
        modifier = Modifier.fillMaxHeight().padding(horizontal = ArxSpacing.xs)
    )
}