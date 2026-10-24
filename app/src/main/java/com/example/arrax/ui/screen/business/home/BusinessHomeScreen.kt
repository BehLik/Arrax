package com.example.arrax.ui.screen.business.home

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIconBadge
import com.example.arrax.core.designsystem.icons.ArxIconShape
import com.example.arrax.core.designsystem.icons.ArxIconStyle
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors
import com.example.arrax.ui.screen.business.BusinessCard
import com.example.arrax.ui.screen.business.BusinessListRow
import com.example.arrax.ui.screen.business.BusinessPill
import com.example.arrax.ui.screen.business.BusinessScreenSurface
import com.example.arrax.ui.screen.business.BusinessSectionHeader
import com.example.arrax.ui.screen.business.BusinessTone
import com.example.arrax.ui.screen.business.BusinessToneBadge
import com.example.arrax.ui.screen.business.BusinessDot
import com.example.arrax.ui.screen.business.color

@Composable
fun BusinessHomeScreen(
    onBack: () -> Unit,
    onCreateEventClick: () -> Unit,
    onEventClick: () -> Unit,
    modifier: Modifier = Modifier,
    onBusinessClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onAlertsClick: () -> Unit = {},
    onCustomersClick: () -> Unit = {},
    onTeamClick: () -> Unit = {},
    onCutsClick: () -> Unit = {},
    viewModel: BusinessHomeViewModel = hiltViewModel()
) {
    BackHandler(onBack = onBack)
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    BusinessHomeContent(
        state = state,
        onBusinessClick = onBusinessClick,
        onNotificationsClick = onNotificationsClick,
        onEventClick = onEventClick,
        onAlertsClick = onAlertsClick,
        onCreateEventClick = onCreateEventClick,
        onCustomersClick = onCustomersClick,
        onTeamClick = onTeamClick,
        onCutsClick = onCutsClick,
        modifier = modifier
    )
}

@Composable
internal fun BusinessHomeContent(
    state: BusinessHomeState,
    onBusinessClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onEventClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onCreateEventClick: () -> Unit,
    onCustomersClick: () -> Unit,
    onTeamClick: () -> Unit,
    onCutsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BusinessScreenSurface(modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = ArxSpacing.md)
                .padding(bottom = ArxSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
        ) {
            HomeHeader(unread = state.unreadNotifications, onNotificationsClick = onNotificationsClick)

            ActiveBusinessCard(state = state, onClick = onBusinessClick)

            Row(horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)) {
                HomeStatCard(
                    title = "Próximos eventos",
                    value = state.upcomingEventsCount.toString(),
                    caption = "Este mes",
                    icon = ArxIcons.Event,
                    modifier = Modifier.weight(1f)
                )
                HomeStatCard(
                    title = "Clientes",
                    value = state.customersCount.toString(),
                    caption = "Registrados",
                    icon = ArxIcons.GroupAdd,
                    modifier = Modifier.weight(1f)
                )
            }

            state.nextEvent?.let { next ->
                BusinessCard(tone = BusinessTone.Warning, onClick = onEventClick, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
                    ) {
                        BusinessToneBadge(icon = ArxIcons.Schedule, tone = BusinessTone.Warning)
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
                            ) {
                                Text("Evento próximo", style = MaterialTheme.typography.titleMedium)
                                BusinessPill(text = next.whenLabel, tone = BusinessTone.Warning)
                            }
                            Text(
                                text = next.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        ArxIcon(icon = ArxIcons.Forward, size = 20.dp)
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                BusinessSectionHeader(
                    title = "Alertas y recordatorios",
                    actionLabel = "Ver todas",
                    onAction = onAlertsClick
                )
                state.alerts.forEach { alert ->
                    BusinessListRow(
                        title = alert.title,
                        subtitle = alert.detail,
                        leading = { BusinessToneBadge(icon = alert.icon, tone = alert.tone) },
                        trailing = { BusinessPill(text = alert.statusLabel, tone = alert.tone) },
                        onClick = onAlertsClick
                    )
                }
            }

            SalesSummary(state = state)

            Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                Text("Acciones rápidas", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                    QuickActionTile("Crear evento", ArxIcons.Event, true, onCreateEventClick, Modifier.weight(1f))
                    QuickActionTile("Clientes", ArxIcons.User, false, onCustomersClick, Modifier.weight(1f))
                    QuickActionTile("Equipo", ArxIcons.TeamOutline, false, onTeamClick, Modifier.weight(1f))
                    QuickActionTile("Cortes", ArxIcons.Box, false, onCutsClick, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(unread: Int, onNotificationsClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = ArxSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Arrax", style = MaterialTheme.typography.headlineLarge)
            Text(
                text = "Negocios. Personas. Resultados.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onNotificationsClick) {
            BadgedBox(badge = { if (unread > 0) Badge { Text(unread.toString()) } }) {
                ArxIcon(
                    icon = if (unread > 0) ArxIcons.NotificationFilled else ArxIcons.Notification,
                    contentDescription = "Notificaciones"
                )
            }
        }
        // TODO: reemplazar por avatar con foto de perfil cuando exista en el state.
        ArxIconBadge(
            icon = ArxIcons.User,
            style = ArxIconStyle.Soft,
            shape = ArxIconShape.Circle,
            contentDescription = "Perfil"
        )
    }
}

@Composable
private fun ActiveBusinessCard(state: BusinessHomeState, onClick: () -> Unit) {
    BusinessCard(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)
        ) {
            ArxIconBadge(
                icon = ArxIcons.Storage,
                style = ArxIconStyle.Solid,
                containerSize = 56.dp,
                iconSize = 28.dp
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "NEGOCIO ACTIVO",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(state.businessName, style = MaterialTheme.typography.headlineMedium)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xxs)
                ) {
                    ArxIcon(icon = ArxIcons.User, tint = MaterialTheme.colorScheme.primary, size = 16.dp)
                    Text(
                        text = "${state.roleLabel} • ${state.memberCount} integrantes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            ArxIcon(icon = ArxIcons.Forward, size = 20.dp)
        }
    }
}

@Composable
private fun HomeStatCard(
    title: String,
    value: String,
    caption: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier
) {
    BusinessCard(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)) {
            ArxIconBadge(icon = icon, style = ArxIconStyle.Soft)
            Column {
                Text(title, style = MaterialTheme.typography.labelLarge)
                Text(value, style = MaterialTheme.typography.headlineLarge)
                Text(
                    text = caption,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SalesSummary(state: BusinessHomeState) {
    Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
        Text("Resumen de ventas (últimos 7 días)", style = MaterialTheme.typography.titleMedium)
        BusinessCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    ArxIcon(icon = ArxIcons.TrendingUp, tint = MaterialTheme.colorScheme.primary)
                    Text(state.salesTotal, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        text = "Total en ventas",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                SalesDonut(slices = state.salesBreakdown)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(ArxSpacing.xxs)
                ) {
                    state.salesBreakdown.forEach { slice ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
                        ) {
                            BusinessDot(tone = slice.tone)
                            Text(
                                text = slice.label,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1
                            )
                            Text(slice.percentLabel, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SalesDonut(slices: List<HomeSalesSliceUi>, modifier: Modifier = Modifier) {
    val colors = slices.map { it.tone.color() }
    val track = MaterialTheme.colorScheme.outlineVariant
    Canvas(modifier = modifier.size(64.dp)) {
        val stroke = 10.dp.toPx()
        val topLeft = Offset(stroke / 2, stroke / 2)
        val arcSize = Size(size.width - stroke, size.height - stroke)
        drawArc(
            color = track, startAngle = 0f, sweepAngle = 360f, useCenter = false,
            topLeft = topLeft, size = arcSize, style = Stroke(stroke)
        )
        var start = -90f
        slices.forEachIndexed { i, slice ->
            val sweep = 360f * slice.fraction
            drawArc(
                color = colors[i], startAngle = start, sweepAngle = sweep, useCenter = false,
                topLeft = topLeft, size = arcSize, style = Stroke(stroke)
            )
            start += sweep
        }
    }
}

@Composable
private fun QuickActionTile(
    label: String,
    @DrawableRes icon: Int,
    highlighted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val glass = MaterialTheme.extendedColors
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = if (highlighted) colors.primary else glass.glass,
        contentColor = if (highlighted) colors.onPrimary else colors.onSurface,
        border = if (highlighted) null else BorderStroke(1.dp, glass.glassBorder)
    ) {
        Column(
            modifier = Modifier
                .clickable(role = Role.Button, onClick = onClick)
                .heightIn(min = 80.dp)
                .padding(ArxSpacing.xs),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ArxIcon(icon = icon, tint = if (highlighted) colors.onPrimary else colors.primary)
            Spacer(Modifier.height(ArxSpacing.xxs))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 900)
@Preview(showBackground = true, widthDp = 360, heightDp = 900, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BusinessHomePreview() {
    ArxTheme(darkTheme = true) {
        BusinessHomeContent(
            state = BusinessHomeState(
                businessName = "Granja San José",
                roleLabel = "Owner",
                memberCount = 8,
                unreadNotifications = 3,
                upcomingEventsCount = 2,
                customersCount = 24,
                nextEvent = HomeNextEventUi(
                    whenLabel = "Hoy • 6:00 PM",
                    message = "Revisa la estimación de cortes y la disponibilidad de insumos para el evento de mañana."
                ),
                alerts = listOf(
                    HomeAlertUi("1", "Evento del sábado", "Faltan 2 días • 6 cerdos estimados", "En preparación", BusinessTone.Success, ArxIcons.Event),
                    HomeAlertUi("2", "Invitación por vencer", "Código #AB12 • Vence en 3 días", "Atención", BusinessTone.Warning, ArxIcons.GroupAdd),
                    HomeAlertUi("3", "Pedidos pendientes de pesar", "Evento del sábado • 4 pedidos", "Pendiente", BusinessTone.Info, ArxIcons.Order)
                ),
                salesTotal = "$ 3,820.00",
                salesBreakdown = listOf(
                    HomeSalesSliceUi("Cortes", "68%", 0.68f, BusinessTone.Primary),
                    HomeSalesSliceUi("Clientes", "22%", 0.22f, BusinessTone.Info),
                    HomeSalesSliceUi("Otros", "10%", 0.10f, BusinessTone.Neutral)
                )
            ),
            onBusinessClick = {}, onNotificationsClick = {}, onEventClick = {}, onAlertsClick = {},
            onCreateEventClick = {}, onCustomersClick = {}, onTeamClick = {}, onCutsClick = {}
        )
    }
}