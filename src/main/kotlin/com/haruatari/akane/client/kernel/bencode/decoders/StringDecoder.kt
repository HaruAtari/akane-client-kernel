package com.haruatari.akane.client.kernel.bencode.decoders

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.dto.StringNode
import kotlin.text.Charsets.UTF_8

internal class StringDecoder(reader: Reader) : NodeDecoder(reader) {
    private enum class State {
        READING_SIZE,
        READING_VALUE,
        COMPLETED
    }

    private var content = mutableListOf<Byte>()
    private var state = State.READING_SIZE
    private var length = 0

    override fun decode(): StringNode {
        content = mutableListOf()
        state = State.READING_SIZE
        length = 0

        while (state != State.COMPLETED) {
            when (state) {
                State.READING_SIZE -> onReadingSize()
                State.READING_VALUE -> onReadingValue()
                State.COMPLETED -> {}
            }
        }

        return StringNode(content.toByteArray())
    }

    private fun onReadingSize() {
        val byte = reader.readNextByte() ?: throw generateException("Unexpected end of file.")

        if (byte in numberTokens) {
            content.add(byte)

            return
        }

        if (byte == stringDelimiterToken) {
            if (content.isEmpty()) {
                throw generateException("The string node should start from the number.")
            }

            length = String(content.toByteArray(), UTF_8).toInt()
            content.clear()
            state = State.READING_VALUE

            return
        }

        throw generateException("The string node should contain the delimiter between its size and value.")
    }

    private fun onReadingValue() {
        val bytes = reader.readNextBytes(length)
        if (bytes.count() != length) {
            throw generateException("The string node's value is shorter then it should be.")
        }

        content = bytes.toMutableList()
        state = State.COMPLETED
    }
}