package com.haruatari.akane.client.kernel.bencode.tokenizer

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.SpecialSymbols
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.StringToken
import kotlin.text.Charsets.UTF_8

internal class StringTokenizer(reader: Reader) : Tokenizer(reader) {
    private enum class State {
        READING_SIZE,
        READING_VALUE,
        COMPLETED
    }

    private var content = mutableListOf<Byte>()
    private var state = State.READING_SIZE
    private var length = 0
    private var initPosition = 0

    override fun tokenize(): StringToken {
        content = mutableListOf()
        state = State.READING_SIZE
        length = 0
        initPosition = reader.getPosition()

        while (state != State.COMPLETED) {
            when (state) {
                State.READING_SIZE -> onReadingSize()
                State.READING_VALUE -> onReadingValue()
                State.COMPLETED -> {}
            }
        }

        return StringToken(content.toByteArray(), initPosition, reader.getPosition() - 1)
    }

    private fun onReadingSize() {
        val byte = reader.readNextByte() ?: throw generateException("Unexpected end of file.")

        if (byte in SpecialSymbols.numberTokens) {
            content.add(byte)

            return
        }

        if (byte == SpecialSymbols.stringDelimiterToken) {
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