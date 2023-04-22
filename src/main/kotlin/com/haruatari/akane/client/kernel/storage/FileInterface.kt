package com.haruatari.akane.client.kernel.storage

import java.io.Closeable

interface FileInterface : Closeable {
    val length: Long

    fun read(offset: Long, length: Int): ByteArray
    fun write(offset: Long, data: ByteArray)
}