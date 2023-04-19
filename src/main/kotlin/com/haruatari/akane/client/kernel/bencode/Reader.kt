package com.haruatari.akane.client.kernel.bencode

import java.io.BufferedInputStream
import java.io.InputStream

internal class Reader(stream: InputStream) {
    private val s: BufferedInputStream
    private var position = 0
    private var lastReadingPosition = 0


    init {
        s = if (stream is BufferedInputStream) stream else BufferedInputStream(stream, 1)
    }

    fun readNextByte(): Byte? {
        val bytes = readNextBytes(1)
        return if (bytes.isEmpty()) null else bytes[0]
    }

    fun readNextBytes(length: Int): ByteArray {
        lastReadingPosition = position

        val bytes = s.readNBytes(length)
        position += bytes.count()

        return bytes;
    }

    fun seeNextByte(): Byte? {
        s.mark(1)
        val bytes = s.readNBytes(1)
        s.reset()

        return if (bytes.isEmpty()) null else bytes[0]
    }

    fun getPosition(): Int = position
    fun getLastReadingPosition(): Int = lastReadingPosition
}