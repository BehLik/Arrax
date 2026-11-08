package com.example.arrax.domain.model

enum class Permission {
    // Eventos
    EVENT_VIEW, EVENT_CREATE, EVENT_EDIT, EVENT_DELETE,
    // Operación
    ORDER_CREATE, PAYMENT_REGISTER, WEIGHING_USE,
    // Clientes
    CUSTOMER_VIEW, CUSTOMER_CREATE, CUSTOMER_EDIT, CUSTOMER_DELETE,
    // Catálogo
    CATALOG_VIEW, CATALOG_EDIT,
    // Negocio y equipo
    BUSINESS_EDIT, BUSINESS_DELETE, MEMBERS_MANAGE, INVITE_CREATE
}

private val adminPermissions = setOf(
    Permission.EVENT_VIEW, Permission.EVENT_CREATE, Permission.EVENT_EDIT, Permission.EVENT_DELETE,
    Permission.ORDER_CREATE, Permission.PAYMENT_REGISTER, Permission.WEIGHING_USE,
    Permission.CUSTOMER_CREATE, Permission.CUSTOMER_DELETE,
    Permission.CATALOG_EDIT, Permission.BUSINESS_EDIT
)

fun Role.permissions(): Set<Permission> = when (this) {
    Role.OWNER -> Permission.entries.toSet()
    Role.ADMIN -> adminPermissions            // sin MEMBERS_MANAGE (solo OWNER, como en las reglas)
    Role.OPERATOR -> setOf(
        Permission.EVENT_VIEW, Permission.ORDER_CREATE, Permission.PAYMENT_REGISTER,
        Permission.WEIGHING_USE, Permission.CUSTOMER_CREATE
    )
    Role.NONE -> emptySet()
}

fun Role.can(permission: Permission) = permission in permissions()