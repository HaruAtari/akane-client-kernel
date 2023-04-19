package com.haruatari.akane.client.kernel.bencode.dto

internal abstract class ValueNode(protected val content: ByteArray) : Node {
    override fun getRawContent(): ByteArray = content

    override fun equals(other: Any?): Boolean = other is ValueNode && other.getRawContent().contentEquals(getRawContent())

    override fun hashCode(): Int = content.contentHashCode()
}