package com.haruatari.akane.client.kernel.bencode.dto

import kotlin.text.Charsets.UTF_8

internal class StringNode(content: ByteArray) : ValueNode(content) {
    override fun getValue(): String = String(content, UTF_8)
}