package com.haruatari.akane.client.kernel.bencode.tokenizer.dto

internal abstract class ValueToken(protected val content: ByteArray, from: Int, to: Int) : Token(from, to) {
    override fun getRawContent(): ByteArray = content

    override fun equals(other: Any?): Boolean = other is ValueToken && other.getRawContent().contentEquals(getRawContent())

    override fun hashCode(): Int = content.contentHashCode()
}