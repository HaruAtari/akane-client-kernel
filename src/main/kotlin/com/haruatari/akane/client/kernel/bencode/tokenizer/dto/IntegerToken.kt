package com.haruatari.akane.client.kernel.bencode.tokenizer.dto

import kotlin.text.Charsets.UTF_8

internal fun IntegerToken(value: Int): IntegerToken {
    return IntegerToken(value.toString().toByteArray(UTF_8))
}

internal class IntegerToken(content: ByteArray) : ValueToken(content) {
    override fun getValue(): Int = String(content, UTF_8).toInt()
}