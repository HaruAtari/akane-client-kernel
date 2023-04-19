package com.haruatari.akane.client.kernel.bencode.decoders

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.dto.StringNode
import kotlin.text.Charsets.UTF_8

internal class StringNodeDecoder(reader: Reader) : NodeDecoder<StringNode>(reader) {
    private enum class State {
        READING_SIZE,
        READING_VALUE,
        COMPLETED
    }

    private val delimiter: Byte = 58 // :
    private val numbers = byteArrayOf(48, 49, 50, 51, 52, 53, 54, 55, 56, 57)
    private var content = mutableListOf<Byte>()
    private var state = State.READING_SIZE
    private var length = 0

    override fun decode(): StringNode {
        while (state != State.COMPLETED) {
            tick()
        }

        return StringNode(content.toByteArray())
    }

    private fun tick() {
        when (state) {
            State.READING_SIZE -> onReadingSize()
            State.READING_VALUE -> onReadingValue()
            State.COMPLETED -> {}
        }
    }

    private fun onReadingSize() {
        val byte = reader.readNextByte() ?: throw generateException("Unexpected end of file.")

        if (byte in numbers) {
            content.add(byte)

            return
        }

        if (byte == delimiter) {
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