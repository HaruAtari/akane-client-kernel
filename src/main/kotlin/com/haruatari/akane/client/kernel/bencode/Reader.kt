package com.haruatari.akane.client.kernel.bencode

import java.io.InputStream

internal class Reader(private val stream: InputStream) {
    private var position = 0
    private var lastReadingPosition = 0

    fun readNextByte(): Byte? {
        val bytes = readNextBytes(1)
        return if (bytes.isEmpty()) null else bytes[0]
    }

    fun readNextBytes(length: Int): ByteArray {
        lastReadingPosition = position

        val bytes = stream.readNBytes(length)
        position += bytes.count()

        return bytes;
    }

    fun seeNextByte(): Byte? {
        stream.mark(1)
        val bytes = stream.readNBytes(1)
        stream.reset()

        return if (bytes.isEmpty()) null else bytes[0]
    }

    fun getPosition(): Int = position
    fun getLastReadingPosition(): Int = lastReadingPosition
}