package com.example.arrax.domain.usecase.order

import com.example.arrax.domain.model.EventCut
import com.example.arrax.domain.model.OrderItem
import com.example.arrax.domain.model.OrderLineInput
import com.example.arrax.domain.model.OrderMath
import com.example.arrax.domain.model.RequestMode


internal fun buildOrderItems(
    orderId: String,
    customerId: String,
    lines: List<OrderLineInput>,
    cuts: List<EventCut>,
): List<OrderItem> {
    require(lines.map { it.cutId }.distinct().size == lines.size) {
        "Hay cortes repetidos en el pedido"
    }
    return lines.map { line ->
        val cut = cuts.firstOrNull { it.cutId == line.cutId }
            ?: throw IllegalArgumentException("Un corte ya no está disponible en este evento")
        val price = cut.pricePerKgCents.toLong()
        val target = when (line.requestedBy) {
            RequestMode.KG -> line.requestedGrams ?: 0
            RequestMode.MONEY -> OrderMath.gramsFromAmount(line.requestedAmountCents ?: 0L, price)
        }
        require(target > 0) { "La cantidad de ${cut.name} debe ser mayor a cero" }

        OrderItem(
            id = OrderMath.itemId(orderId, line.cutId),
            orderId = orderId,
            customerId = customerId,
            cutId = cut.cutId,
            cutName = cut.name,
            pricePerKgCents = price,
            requestedBy = line.requestedBy,
            requestedGrams = line.requestedGrams.takeIf { line.requestedBy == RequestMode.KG },
            requestedAmountCents = line.requestedAmountCents.takeIf { line.requestedBy == RequestMode.MONEY },
            targetGrams = target
        )
    }
}