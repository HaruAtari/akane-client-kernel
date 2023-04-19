package com.haruatari.akane.client.kernel.bencode.dto

import kotlin.text.Charsets.UTF_8

internal class IntNode(private val content: ByteArray) : Node<Int> {
    override fun getValue(): Int {
        return String(content, UTF_8).toInt()
    }

    override fun getRawContent(): ByteArray {
        return content
    }
}