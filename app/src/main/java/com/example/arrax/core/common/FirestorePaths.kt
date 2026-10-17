package com.example.arrax.core.common

/** Single source of truth for Firestore names. Must match firebase/firestore.rules. */
object FirestorePaths {
    const val USERS = "users"               // was: usuarios
    const val TENANTS = "tenants"
    const val CUT_CATALOG = "cutCatalog"    // was: catalogoCortes
    const val EVENTS = "events"
    const val EVENT_CUTS = "eventCuts"      // was: cortesEvento (in rules)
}