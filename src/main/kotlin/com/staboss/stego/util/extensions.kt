package com.staboss.stego.util

import kotlin.system.exitProcess

/**
 * Supplements the binary sequence with zeros
 *
 * @param bits coding (bits per symbol)
 * @return binary representation multiple of [bits]
 */
fun String.toCorrectBinaryLength(bits: Int = 8) = "0".repeat(bits - length % bits) + this

/**
 * Converts a number to its binary format
 *
 * @param bits coding (bits per symbol)
 * @return binary representation of a number
 */
fun Int.toBinary(bits: Int = 8): String = toString(radix = 2).toCorrectBinaryLength(bits)

/**
 * Converts a character to its binary format
 *
 * @param bits coding (bits per symbol)
 * @return binary representation of a character
 */
fun Char.toBinary(bits: Int = 8): String = toInt().toString(radix = 2).toCorrectBinaryLength(bits)

/**
 * Converts a string to binary representation
 *
 * @param bits coding (bits per symbol)
 * @return binary string
 */
fun String.toBinary(bits: Int = 8): String =
    map { char -> char.toBinary(bits) }
        .flatMap { binaryString -> binaryString.toList() }
        .map { bit -> bit.toString().toInt() }
        .joinToString("")

/**
 * Converts a string of bits to text
 *
 * @param bits coding (bits per symbol)
 * @return text
 */
fun String.toText(bits: Int = 8): String =
    chunked(bits)
        .map { binaryString -> binaryString.toInt(radix = 2).toChar() }
        .joinToString("")


/**
 * Casts List<*> to List<T>
 *
 * @param T the type of elements contained in the list
 * @return if cast is successful List<T> otherwise NULL
 */
inline fun <reified T> List<*>.asListOfType(): List<T>? {
    return if (all { it is T }) {
        @Suppress("UNCHECKED_CAST")
        this as List<T>
    } else {
        null
    }
}

/**
 * Requires [value] condition or exits with given [lazyMessage]
 *
 * @param value condition
 * @param lazyMessage exit message if condition is false
 */
inline fun requireOrExit(value: Boolean, lazyMessage: () -> Any) {
    if (!value) {
        val message = lazyMessage.invoke()
        System.err.println(message.toString())
        exitProcess(-1)
    }
}

/**
 * Measures [action] execution time
 *
 * @param action action
 * @return time (ms)
 */
inline fun measureTime(action: () -> Unit): Long {
    val startTime = System.currentTimeMillis()
    action.invoke()
    val endTime = System.currentTimeMillis()
    return endTime - startTime
}
