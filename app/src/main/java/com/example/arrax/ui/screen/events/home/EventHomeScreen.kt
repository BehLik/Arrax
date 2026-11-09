package com.example.arrax.ui.screen.events.home

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.arrax.core.common.Money
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.components.display.ArxSectionHeader
import com.example.arrax.core.designsystem.components.feedback.ArxChipStatus
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.components.navigation.ArxBottomBar
import com.example.arrax.core.designsystem.components.navigation.ArxBottomBarItem
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIconBadge
import com.example.arrax.core.designsystem.icons.ArxIconShape
import com.example.arrax.core.designsystem.icons.ArxIconStyle
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.images.ArxImages
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

// ─────────────────────────────────────────────────────────────────────────────
// Screen conectada al ViewModel
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun EventHomeScreen(
    onBack: () -> Unit,
    onEditEventClick: () -> Unit,
    onEditEventNameClick: () -> Unit,
    onMakeOrdersClick: () -> Unit,
    onHandsFreeWeighingClick: () -> Unit,
    onMakeDeliveriesClick: () -> Unit,
    onSeeDetailClick: () -> Unit,
    onSeeAllOrdersClick: () -> Unit,
    onOrderClick: (String) -> Unit,
    onBottomBarNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventHomeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val handleEvent by rememberUpdatedState<(EventHomeEvent) -> Unit> { event ->
        when (event) {
            EventHomeEvent.NavigateBack -> onBack()
            EventHomeEvent.EditEvent -> onEditEventClick()
            EventHomeEvent.EditEventName -> onEditEventNameClick()
            EventHomeEvent.MakeOrders -> onMakeOrdersClick()
            EventHomeEvent.HandsFreeWeighing -> onHandsFreeWeighingClick()
            EventHomeEvent.MakeDeliveries -> onMakeDeliveriesClick()
            EventHomeEvent.SeeDetail -> onSeeDetailClick()
            EventHomeEvent.SeeAllOrders -> onSeeAllOrdersClick()
            is EventHomeEvent.OpenOrder -> onOrderClick(event.orderId)
            is EventHomeEvent.NavigateToRoute -> onBottomBarNavigate(event.route)
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { handleEvent(it) }
    }

    EventHomeContent(
        state = state,
        onBackClick = viewModel::onBackClick,
        onEditEventClick = viewModel::onEditEventClick,
        onEditEventNameClick = viewModel::onEditEventNameClick,
        onMakeOrdersClick = viewModel::onMakeOrdersClick,
        onHandsFreeWeighingClick = viewModel::onHandsFreeWeighingClick,
        onMakeDeliveriesClick = viewModel::onMakeDeliveriesClick,
        onSeeDetailClick = viewModel::onSeeDetailClick,
        onSeeAllOrdersClick = viewModel::onSeeAllOrdersClick,
        onOrderClick = viewModel::onOrderClick,
        onBottomBarClick = viewModel::onBottomBarClick,
        modifier = modifier
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Contenido stateless
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventHomeContent(
    state: EventHomeState,
    onBackClick: () -> Unit,
    onEditEventClick: () -> Unit,
    onEditEventNameClick: () -> Unit,
    onMakeOrdersClick: () -> Unit,
    onHandsFreeWeighingClick: () -> Unit,
    onMakeDeliveriesClick: () -> Unit,
    onSeeDetailClick: () -> Unit,
    onSeeAllOrdersClick: () -> Unit,
    onOrderClick: (String) -> Unit,
    onBottomBarClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val bottomItems = remember {
        listOf(
            ArxBottomBarItem(
                label = "Inicio",
                iconRes = ArxIcons.HomeOutline,
                selectedIconRes = ArxIcons.HomeFilled,
                route = EventHomeState.ROUTE_HOME
            ),
            ArxBottomBarItem(
                label = "Pedidos",
                iconRes = ArxIcons.Order,
                route = EventHomeState.ROUTE_ORDERS
            ),
            ArxBottomBarItem(
                label = "Entregas",
                iconRes = ArxIcons.Delivery,
                route = EventHomeState.ROUTE_DELIVERIES
            ),
            ArxBottomBarItem(
                label = "Más",
                iconRes = ArxIcons.More,
                route = EventHomeState.ROUTE_MORE
            )
        )
    }

    ArxBackground(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            ArxTopBar(
                title = "Evento",
                centeredTitle = false,
                navigationType = ArxTopBarNavigationType.BACK,
                onNavigationClick = onBackClick,
                actions = {
                    EditPill(
                        onClick = onEditEventClick,
                        modifier = Modifier.padding(end = ArxSpacing.sm)
                    )
                }
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = ArxSpacing.md,
                    end = ArxSpacing.md,
                    top = ArxSpacing.sm,
                    bottom = 104.dp
                ),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
            ) {
                item(key = "hero") {
                    EventHeroCard(state = state, onEditNameClick = onEditEventNameClick)
                }

                item(key = "quick_actions") {
                    QuickActionsRow(
                        onMakeOrdersClick = onMakeOrdersClick,
                        onHandsFreeWeighingClick = onHandsFreeWeighingClick,
                        onMakeDeliveriesClick = onMakeDeliveriesClick
                    )
                }

                item(key = "summary") {
                    EventSummaryCard(state = state, onSeeDetailClick = onSeeDetailClick)
                }

                item(key = "recent_header") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ArxIcon(
                            icon = ArxIcons.List,
                            tint = MaterialTheme.colorScheme.primary,
                            size = 20.dp
                        )
                        Spacer(Modifier.width(ArxSpacing.xs))
                        ArxSectionHeader(
                            title = "Pedidos recientes",
                            actionLabel = "Ver todos",
                            onAction = onSeeAllOrdersClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                items(state.recentOrders, key = { it.id }) { order ->
                    RecentOrderRowCard(order = order, onClick = { onOrderClick(order.id) })
                }
            }
        }

        ArxBottomBar(
            items = bottomItems,
            currentRoute = state.currentRoute,
            onItemClick = onBottomBarClick,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// TopBar: píldora "Editar"
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun EditPill(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        color = MaterialTheme.extendedColors.glass,
        border = BorderStroke(1.dp, MaterialTheme.extendedColors.glassBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ArxIcon(
                icon = ArxIcons.Settings,
                tint = MaterialTheme.colorScheme.primary,
                size = 16.dp
            )
            Spacer(Modifier.width(ArxSpacing.xs))
            Text(text = "Editar", style = MaterialTheme.typography.labelLarge)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Hero card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun EventHeroCard(
    state: EventHomeState,
    onEditNameClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArxCard(variant = ArxCardVariant.Glass, modifier = modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Capa decorativa derecha con fundido hacia la izquierda
            Image(
                painter = painterResource(id = ArxImages.CutMeat),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxWidth(0.55f)
                    .height(170.dp)
                    .alpha(0.85f)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxWidth(0.55f)
                    .height(170.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(modifier = Modifier.fillMaxWidth().padding(ArxSpacing.md)) {
                // El punto verde lo dibuja ArxStatusChip solo (status != Neutral)
                ArxStatusChip(text = state.statusText, status = ArxChipStatus.Success)

                Spacer(Modifier.height(ArxSpacing.xs))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = state.eventName,
                        style = MaterialTheme.typography.headlineLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(Modifier.width(ArxSpacing.xs))
                    Surface(
                        onClick = onEditNameClick,
                        shape = CircleShape,
                        color = MaterialTheme.extendedColors.glass,
                        border = BorderStroke(1.dp, MaterialTheme.extendedColors.glassBorder),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            ArxIcon(
                                icon = ArxIcons.Edit,
                                tint = MaterialTheme.colorScheme.onSurface,
                                size = 16.dp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(ArxSpacing.xs))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    ArxIcon(
                        icon = ArxIcons.Date,
                        size = 16.dp,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(ArxSpacing.xxs))
                    Text(
                        text = state.dateFormatted,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(ArxSpacing.sm))
                    VerticalDivider(
                        modifier = Modifier.height(14.dp),
                        color = MaterialTheme.extendedColors.glassBorder
                    )
                    Spacer(Modifier.width(ArxSpacing.sm))
                    ArxIcon(
                        icon = ArxIcons.Piggy,
                        size = 16.dp,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(ArxSpacing.xxs))
                    Text(
                        text = "${state.pigsCount} cerdos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(ArxSpacing.md))
                HorizontalDivider(color = MaterialTheme.extendedColors.glassBorder)
                Spacer(Modifier.height(ArxSpacing.sm))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ArxIcon(
                            icon = ArxIcons.Inventory,
                            size = 16.dp,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(ArxSpacing.xxs))
                        Text(
                            text = "Cortes configurados: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${state.configuredCutsCount}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ArxIcon(
                            icon = ArxIcons.User,
                            size = 16.dp,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.width(ArxSpacing.xxs))
                        Text(
                            text = "Creado por: ${state.createdBy}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Acciones rápidas
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun QuickActionsRow(
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

// ─────────────────────────────────────────────────────────────────────────────
// Resumen del evento
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun EventSummaryCard(
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
                    value = "${state.pigsCount}",
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

// ─────────────────────────────────────────────────────────────────────────────
// Pedido reciente
// ─────────────────────────────────────────────────────────────────────────────

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
private fun RecentOrderRowCard(
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

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun EventHomePreviewContent() {
    EventHomeContent(
        state = EventHomeState(),
        onBackClick = {},
        onEditEventClick = {},
        onEditEventNameClick = {},
        onMakeOrdersClick = {},
        onHandsFreeWeighingClick = {},
        onMakeDeliveriesClick = {},
        onSeeDetailClick = {},
        onSeeAllOrdersClick = {},
        onOrderClick = {},
        onBottomBarClick = {}
    )
}

@Preview(name = "EventHome Light", showBackground = true, widthDp = 390, heightDp = 900)
@Composable
private fun EventHomeScreenLightPreview() {
    ArxTheme(darkTheme = false) { EventHomePreviewContent() }
}

@Preview(
    name = "EventHome Dark",
    showBackground = true,
    widthDp = 390,
    heightDp = 900,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun EventHomeScreenDarkPreview() {
    ArxTheme(darkTheme = true) { EventHomePreviewContent() }
}