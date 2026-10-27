package com.example.arrax.core.common

object InviteCode {
    private const val ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
    private val random = java.security.SecureRandom()

    fun generate(length: Int = 8): String = buildString(length) {
        repeat(length) { append(ALPHABET[random.nextInt(ALPHABET.length)]) }
    }

    /** "K7M29QPX" -> "K7M2-9QPX" (solo para mostrar). */
    fun display(code: String) = code.chunked(4).joinToString("-")

    /** Para el canje: acepta minúsculas, guiones y espacios. */
    fun normalize(input: String) = input.uppercase().filter { it.isLetterOrDigit() }

    /** Contenido del QR; el escáner del canje lo parsea. */
    fun deepLink(code: String) = "arrax://join?code=$code"
}