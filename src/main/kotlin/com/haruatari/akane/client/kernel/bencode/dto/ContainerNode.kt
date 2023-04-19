package com.haruatari.akane.client.kernel.bencode.dto

import com.haruatari.akane.client.kernel.bencode.excetions.DecoderException

internal abstract class ContainerNode() : Node {
    override fun getRawContent(): ByteArray {
        throw DecoderException("The list node doesn't support that method.")
    }

    override fun equals(other: Any?): Boolean = other is ContainerNode && other.getValue() == getValue()

    override fun hashCode(): Int = getValue().hashCode()
}