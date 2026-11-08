package com.example.arrax.domain.model

enum class Permission {
    EVENT_VIEW, EVENT_CREATE, EVENT_EDIT, EVENT_DELETE,
    ORDER_CREATE, PAYMENT_REGISTER, WEIGHING_USE,
    CUSTOMER_VIEW, CUSTOMER_CREATE, CUSTOMER_EDIT, CUSTOMER_DELETE,
    CATALOG_VIEW, CATALOG_EDIT,
    BUSINESS_EDIT, BUSINESS_DELETE, MEMBERS_MANAGE, INVITE_CREATE
}

private val operatorPermissions = setOf(
    Permission.EVENT_VIEW,
    Permission.ORDER_CREATE, Permission.PAYMENT_REGISTER, Permission.WEIGHING_USE,
    Permission.CUSTOMER_VIEW, Permission.CUSTOMER_CREATE, Permission.CUSTOMER_EDIT,
    Permission.CATALOG_VIEW
)

private val adminPermissions = operatorPermissions + setOf(
    Permission.EVENT_CREATE, Permission.EVENT_EDIT, Permission.EVENT_DELETE,
    Permission.CUSTOMER_DELETE,
    Permission.CATALOG_EDIT,
    Permission.BUSINESS_EDIT, Permission.INVITE_CREATE
)

private val permissionsByRole: Map<Role, Set<Permission>> = mapOf(
    Role.OWNER to Permission.entries.toSet(),
    Role.ADMIN to adminPermissions,      // sin MEMBERS_MANAGE ni BUSINESS_DELETE
    Role.OPERATOR to operatorPermissions,
    Role.NONE to emptySet()
)

fun Role.permissions(): Set<Permission> = permissionsByRole.getValue(this)
fun Role.can(permission: Permission) = permission in permissions()