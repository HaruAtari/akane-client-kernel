package com.haruatari.akane.client.kernel.bencode

internal object SpecialSymbols {
    const val listBeginToken: Byte = 108 // l
    const val dictionaryBeginToken: Byte = 100; // d
    const val intBeginToken: Byte = 105; // i
    const val endToken: Byte = 101 // e
    const val stringDelimiterToken: Byte = 58 // :
    val numberTokens = byteArrayOf(48, 49, 50, 51, 52, 53, 54, 55, 56, 57)
    const val minusToken: Byte = 45 // -
}