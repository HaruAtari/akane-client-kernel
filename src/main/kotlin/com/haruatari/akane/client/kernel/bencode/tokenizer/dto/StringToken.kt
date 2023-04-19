package com.haruatari.akane.client.kernel.bencode.tokenizer.dto

import kotlin.text.Charsets.UTF_8

internal class StringToken(content: ByteArray) : ValueToken(content) {
    override fun getValue(): String = String(content, UTF_8)
}