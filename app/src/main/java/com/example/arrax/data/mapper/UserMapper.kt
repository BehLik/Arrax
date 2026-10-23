package com.example.arrax.data.mapper

import com.example.arrax.domain.model.Role
import com.example.arrax.domain.model.SignInMethod
import com.example.arrax.domain.model.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toUser(): User? {
    if (!exists()) return null
    return User(
        uid = id,
        name = getString("name") ?: "",
        // null = user without a business (User.tenantId is String?), never ""
        tenantId = getString("tenantId")?.takeIf { it.isNotBlank() },
        role = getString("role").toRole()
    )
}
private const val GOOGLE_PROVIDER = "google.com"

fun FirebaseUser.sessionPhotoUrl(sizePx: Int = 256): String? {
    val raw = providerData.firstOrNull { it.providerId == GOOGLE_PROVIDER }?.photoUrl
        ?: photoUrl
    return raw?.toString()?.replace(Regex("=s\\d+-c$"), "=s$sizePx-c")
}
fun FirebaseUser.signInMethods(): Set<SignInMethod> = providerData.mapNotNull {
    when (it.providerId) {
        EmailAuthProvider.PROVIDER_ID -> SignInMethod.PASSWORD
        GoogleAuthProvider.PROVIDER_ID -> SignInMethod.GOOGLE
        else -> null
    }
}.toSet()
/** Unknown or missing role must NEVER fall back to ADMIN. "OPERADOR" is legacy data. */
val Role.isAdminOrOwner: Boolean get() = this == Role.OWNER || this == Role.ADMIN
internal fun String?.toRole(): Role = when (this?.uppercase()) {

    "OWNER" -> Role.OWNER
    "ADMIN" -> Role.ADMIN
    "OPERATOR", "OPERADOR" -> Role.OPERATOR
    else -> Role.NONE
}
