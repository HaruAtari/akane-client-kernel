package com.haruatari.akane.client.kernel.bencode.dto

internal interface Node {
    fun getValue(): Any
    fun getRawContent(): ByteArray
}