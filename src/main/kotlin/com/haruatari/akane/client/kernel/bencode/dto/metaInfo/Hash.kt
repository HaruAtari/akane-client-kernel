package com.haruatari.akane.client.kernel.bencode.dto.metaInfo

import java.util.*

fun Hash(hex: String): Hash {
    check(hex.length % 2 == 0) { "HEX must have an even length" }

    return Hash(hex.lowercase().chunked(2).map { it.toInt(16).toByte() }.toByteArray())
}

data class Hash(private val bytes: ByteArray) {
    fun getBytes(): ByteArray = bytes
    fun getHex(): String = HexFormat.of().formatHex(bytes)

    override fun equals(other: Any?): Boolean {
        if (other is String) return other.lowercase() == getHex()
        if (other is ByteArray) return other.contentEquals(getBytes())
        if (other !is Hash) return false

        return other.getBytes().contentEquals(getBytes())
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }

    override fun toString(): String = "Hash(hex=" + getHex() + ")"
}