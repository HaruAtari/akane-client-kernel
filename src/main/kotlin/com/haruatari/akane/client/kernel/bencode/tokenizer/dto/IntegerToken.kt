package com.haruatari.akane.client.kernel.bencode.tokenizer.dto

import kotlin.text.Charsets.UTF_8

internal fun IntegerToken(value: Int): IntegerToken {
    return IntegerToken(value.toString().toByteArray(UTF_8), 0, 0)
}

internal fun IntegerToken(value: ByteArray): IntegerToken {
    return IntegerToken(value, 0, 0)
}

internal class IntegerToken(content: ByteArray, from: Int, to: Int) : ValueToken(content, from, to) {
    override fun getValue(): Int = String(content, UTF_8).toInt()
    override fun toString(): String = "IntegerToken(from: $from; to: $to; value: " + getValue() + ")"
}