package com.haruatari.akane.client.kernel.bencode.tokenizer.dto

import kotlin.text.Charsets.UTF_8

internal fun NumberToken(value: Long): NumberToken {
    return NumberToken(value.toString().toByteArray(UTF_8), 0, 0)
}

internal fun NumberToken(value: ByteArray): NumberToken {
    return NumberToken(value, 0, 0)
}

internal class NumberToken(content: ByteArray, from: Int, to: Int) : ValueToken(content, from, to) {
    override fun getValue(): Long = String(content, UTF_8).toLong()
    override fun toString(): String = "IntegerToken(from: $from; to: $to; value: " + getValue() + ")"
}