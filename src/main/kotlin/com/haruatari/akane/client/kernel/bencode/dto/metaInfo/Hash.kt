package com.haruatari.akane.client.kernel.bencode.dto.metaInfo

import java.util.*

 fun Hash(hex: String): Hash {
     check(hex.length % 2 == 0) { "HEX must have an even length" }

     return Hash(hex.lowercase().chunked(2).map { it.toInt(16).toByte() }.toByteArray())
 }

data class Hash(private val bytes: ByteArray) : HashInterface {
    private val urlSafeBytes: ByteArray = byteArrayOf(
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57, // 0-9
        65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, // A-Z
        97, 98, 99, 100, 101, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, // a-z
        45, 46, 95, 126// . - _ ~
    )

    override fun getBytes(): ByteArray = bytes

    override fun getHex(): String = HexFormat.of().formatHex(bytes)

    override fun getUrlEncoded(): String {
        val result = StringBuilder(bytes.size)

        for (byte in getBytes()) {
            if (urlSafeBytes.contains(byte)) {
                result.append(byte.toInt().toChar())
            } else {
                result.append('%')
                result.append(HexFormat.of().formatHex(byteArrayOf(byte)))
            }
        }

        return result.toString()
    }

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