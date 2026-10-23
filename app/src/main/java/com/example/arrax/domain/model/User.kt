package com.example.arrax.domain.model

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val tenantId: String? = null,
    val role: Role = Role.NONE,
    val photoUrl: String? = null,
    val signInMethods: Set<SignInMethod> = emptySet()
) {
    val hasPassword: Boolean get() = SignInMethod.PASSWORD in signInMethods
}
