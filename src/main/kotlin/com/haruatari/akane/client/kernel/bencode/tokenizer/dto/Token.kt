package com.haruatari.akane.client.kernel.bencode.tokenizer.dto

internal abstract class Token(protected val from: Int, protected val to: Int) {
    abstract fun getValue(): Any
    abstract fun getRawContent(): ByteArray
    abstract override fun toString(): String
}