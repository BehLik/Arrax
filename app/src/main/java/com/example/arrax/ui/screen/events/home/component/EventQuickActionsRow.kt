package com.example.arrax.ui.screen.events.home.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing

@Composable
internal fun EventQuickActionsRow(
    onMakeOrdersClick: () -> Unit,
    onHandsFreeWeighingClick: () -> Unit,
    onMakeDeliveriesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
    ) {
        EventQuickActionCard(
            iconRes = ArxIcons.Order,
            title = "Hacer\nPedidos",
            subtitle = "Registra nuevos pedidos",
            onClick = onMakeOrdersClick,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
        EventQuickActionCard(
            iconRes = ArxIcons.Mic,
            title = "Pesajes\n(Manos Libres)",
            subtitle = "Confirma pedidos por voz",
            onClick = onHandsFreeWeighingClick,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
        EventQuickActionCard(
            iconRes = ArxIcons.Delivery,
            title = "Hacer\nEntregas",
            subtitle = "Marca entregas realizadas",
            onClick = onMakeDeliveriesClick,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
    }
}

@Composable
private fun EventQuickActionCard(
    @DrawableRes iconRes: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArxCard(
        modifier = modifier,
        onClick = onClick,
        variant = ArxCardVariant.Glass
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(ArxSpacing.sm)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                ArxIcon(
                    icon = iconRes,
                    tint = MaterialTheme.colorScheme.primary,
                    size = 32.dp
                )
                Spacer(Modifier.height(ArxSpacing.xs))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    minLines = 2
                )
                Spacer(Modifier.height(ArxSpacing.xxs))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }
            Box(
                modifier = Modifier.align(Alignment.CenterEnd).size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                ArxIcon(
                    icon = ArxIcons.Forward,
                    size = 14.dp,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}