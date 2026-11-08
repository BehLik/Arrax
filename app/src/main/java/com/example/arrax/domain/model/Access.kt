package com.example.arrax.domain.model

sealed interface Access {
    data object Loading : Access
    data object None : Access   // no es miembro, fue expulsado o el rol es inválido
    data class Granted(val role: Role) : Access {
        fun can(permission: Permission) = role.can(permission)
    }
}