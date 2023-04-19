package com.haruatari.akane.client.kernel.bencode.dto

internal interface Node<T> {
    fun getValue(): T
    fun getRawContent(): ByteArray
}