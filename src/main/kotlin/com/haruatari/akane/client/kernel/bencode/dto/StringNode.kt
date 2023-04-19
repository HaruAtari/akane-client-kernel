package com.haruatari.akane.client.kernel.bencode.dto

import kotlin.text.Charsets.UTF_8

internal class StringNode(private val content: ByteArray) : Node<String> {
    override fun getValue(): String {
        return String(content, UTF_8)
    }

    override fun getRawContent(): ByteArray {
        return content
    }
}