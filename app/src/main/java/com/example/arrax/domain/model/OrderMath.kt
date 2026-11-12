package com.example.arrax.domain.model

object OrderMath {
    fun gramsFromAmount(amountCents: Long, pricePerKgCents: Long): Int =
        if (pricePerKgCents > 0) ((amountCents * 1000 + pricePerKgCents / 2) / pricePerKgCents).toInt() else 0

    fun amountFromGrams(grams: Int, pricePerKgCents: Long): Long =
        (grams * pricePerKgCents + 500) / 1000

    // Se cobra SIEMPRE por peso real
    fun itemTotalCents(item: OrderItem) = amountFromGrams(item.weighedGrams, item.pricePerKgCents)
    fun customerTotalCents(items: List<OrderItem>) = items.sumOf { itemTotalCents(it) }
}