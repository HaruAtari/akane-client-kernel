package com.haruatari.akane.client.kernel.bencode.tokenizer.dto

internal interface Token {
    fun getValue(): Any
    fun getRawContent(): ByteArray
}