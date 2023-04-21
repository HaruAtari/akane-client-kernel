package com.haruatari.akane.client.kernel.storage

interface FileInterface {
    fun read(offset: Long, length: Int): ByteArray
    fun write(offset: Long, data: ByteArray)
}