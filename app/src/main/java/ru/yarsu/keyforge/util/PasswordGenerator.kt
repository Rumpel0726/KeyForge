package ru.yarsu.keyforge.util

import java.security.SecureRandom

object PasswordGenerator {

    private const val LOWERCASE = "abcdefghijklmnopqrstuvwxyz"
    private const val UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val DIGITS = "0123456789"
    private const val SPECIAL = "!@#\$%^&*()-_=+[]{}|;:',.<>?/"

    fun generate(
        length: Int = 16,
        useLowercase: Boolean = true,
        useUppercase: Boolean = true,
        useDigits: Boolean = true,
        useSpecial: Boolean = false
    ): String {
        val charPool = buildString {
            if (useLowercase) append(LOWERCASE)
            if (useUppercase) append(UPPERCASE)
            if (useDigits) append(DIGITS)
            if (useSpecial) append(SPECIAL)
        }
        if (charPool.isEmpty()) return ""

        val random = SecureRandom()
        return (1..length)
            .map { charPool[random.nextInt(charPool.length)] }
            .joinToString("")
    }
}
