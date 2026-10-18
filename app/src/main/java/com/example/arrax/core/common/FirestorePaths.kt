package com.example.arrax.core.common

/**
 * Single source of truth for Firestore names. Must match firebase/firestore.rules
 * (see arrax_firestore_map.md). Constants for later phases are harmless to keep now.
 */
object FirestorePaths {
    // Top level
    const val USERS = "users"               // was: usuarios
    const val INVITES = "invites"           // F2
    const val TENANTS = "tenants"

    // users/{uid}/...
    const val NOTIFICATIONS = "notifications"

    // tenants/{tenantId}/...
    const val MEMBERS = "members"           // F2
    const val CUT_CATALOG = "cutCatalog"    // was: catalogoCortes
    const val CUSTOMERS = "customers"       // F4
    const val EVENTS = "events"

    // tenants/{tenantId}/events/{eventId}/...
    const val EVENT_CUTS = "eventCuts"      // was: cortesEvento (in rules)
    const val STATS = "stats"               // F4
    const val ORDERS = "orders"             // F4
    const val ORDER_ITEMS = "orderItems"    // F4
    const val PAYMENTS = "payments"         // F4
    const val EXPENSES = "expenses"         // F5

    // Fixed document ids
    const val STATS_LIVE = "live"
}

/** Field names used in queries (collectionGroup / where / orderBy). */
object FirestoreFields {
    const val UID = "uid"
    const val TENANT_ID = "tenantId"
    const val EVENT_ID = "eventId"
    const val ORDER_ID = "orderId"
    const val CUSTOMER_ID = "customerId"
    const val CUT_ID = "cutId"
    const val STATUS = "status"
    const val STATE = "state"
    const val DATE = "date"
    const val CREATED_AT = "createdAt"
}
