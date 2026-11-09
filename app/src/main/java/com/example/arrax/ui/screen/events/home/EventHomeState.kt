package com.example.arrax.ui.screen.events.home

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.example.arrax.core.designsystem.images.ArxImages

/** Estado de un pedido tal como se muestra en "Pedidos recientes". */
enum class RecentOrderStatus { DELIVERED, IN_TRANSIT, PENDING }

/** Modelo de UI de un pedido reciente. El formateo de dinero se hace en la UI con Money.format. */
@Immutable
data class RecentOrderUi(
    val id: String,
    val customerName: String,
    val address: String,
    val cutName: String,
    @DrawableRes val cutImageRes: Int,
    val weightLabel: String,
    val totalCents: Long,
    val status: RecentOrderStatus,
    val timeLabel: String
)

@Immutable
data class EventHomeState(
    val eventName: String = "Neos",
    val statusText: String = "Activo",
    val dateFormatted: String = "26 oct. 2026",
    val pigsCount: Int = 2,
    val configuredCutsCount: Int = 5,
    val createdBy: String = "Tú",
    val ordersCount: Int = 18,
    val totalSalesCents: Long = 324_050L,
    val recentOrders: List<RecentOrderUi> = defaultRecentOrders,
    val currentRoute: String = ROUTE_HOME
) {
    companion object {
        const val ROUTE_HOME = "event_home"
        const val ROUTE_ORDERS = "event_orders"
        const val ROUTE_DELIVERIES = "event_deliveries"
        const val ROUTE_MORE = "event_more"
    }
}

private val defaultRecentOrders = listOf(
    RecentOrderUi(
        id = "order_1",
        customerName = "Juan Pérez",
        address = "Calle 123 #45",
        cutName = "Costilla",
        cutImageRes = ArxImages.CutRibs,
        weightLabel = "2.5 kg",
        totalCents = 40_250L,
        status = RecentOrderStatus.DELIVERED,
        timeLabel = "Hoy • 5:12 PM"
    ),
    RecentOrderUi(
        id = "order_2",
        customerName = "María López",
        address = "Av. Principal #123",
        cutName = "Espinazo",
        cutImageRes = ArxImages.CutBackbone,
        weightLabel = "1.0 kg",
        totalCents = 7_600L,
        status = RecentOrderStatus.IN_TRANSIT,
        timeLabel = "Hoy • 4:20 PM"
    ),
    RecentOrderUi(
        id = "order_3",
        customerName = "Roberto Castillo",
        address = "Calle 56 #78",
        cutName = "Pecho",
        cutImageRes = ArxImages.CutMeat,
        weightLabel = "3.0 kg",
        totalCents = 22_800L,
        status = RecentOrderStatus.PENDING,
        timeLabel = "Hoy • 3:45 PM"
    )
)