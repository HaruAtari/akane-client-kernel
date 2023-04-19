package com.haruatari.akane.client.kernel.bencode.dto

import kotlin.text.Charsets.UTF_8

internal class IntegerNode(content: ByteArray) : ValueNode(content) {
    override fun getValue(): Int = String(content, UTF_8).toInt()
}