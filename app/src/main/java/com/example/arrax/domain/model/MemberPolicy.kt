package com.example.arrax.domain.model

fun canManageMember(actor: Role, target: Role): Boolean = when (actor) {
    Role.OWNER -> target != Role.OWNER
    Role.ADMIN -> target == Role.OPERATOR
    else -> false
}

fun assignableRoles(actor: Role, target: Role): List<Role> = when {
    !canManageMember(actor, target) -> emptyList()
    actor == Role.OWNER -> listOf(Role.ADMIN, Role.OPERATOR) - target
    else -> listOf(Role.ADMIN) - target
}