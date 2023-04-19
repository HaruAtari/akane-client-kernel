package com.haruatari.akane.client.kernel.bencode.tokenizer.dto

import kotlin.text.Charsets.UTF_8

internal fun StringToken(value: String): StringToken {
    return StringToken(value.toByteArray(UTF_8), 0, 0)
}

internal fun StringToken(value: ByteArray): StringToken {
    return StringToken(value, 0, 0)
}

internal class StringToken(content: ByteArray, from: Int, to: Int) : ValueToken(content, from, to) {
    override fun getValue(): String = String(content, UTF_8)
    override fun toString(): String = "StringToken(from: $from; to: $to; value: " + getValue() + ")"
}