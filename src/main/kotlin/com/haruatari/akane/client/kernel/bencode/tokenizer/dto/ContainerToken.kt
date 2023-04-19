package com.haruatari.akane.client.kernel.bencode.tokenizer.dto

import com.haruatari.akane.client.kernel.bencode.excetions.TokenizerException

internal abstract class ContainerToken() : Token {
    override fun getRawContent(): ByteArray {
        throw TokenizerException("The list node doesn't support that method.")
    }

    override fun equals(other: Any?): Boolean = other is ContainerToken && other.getValue() == getValue()

    override fun hashCode(): Int = getValue().hashCode()
}