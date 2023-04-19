package com.haruatari.akane.client.kernel.bencode.tokenizer

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.ListToken
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.Token

internal class ListTokenizer(reader: Reader) : Tokenizer(reader) {
    private enum class State {
        READ_NOTHING,
        READ_BEGINNING_TOKEN,
        READ_VALUE,
        READ_END_TOKEN
    }

    private var state = State.READ_NOTHING
    private var content = mutableListOf<Token>()

    override fun tokenize(): ListToken {
        content = mutableListOf()
        state = State.READ_NOTHING

        while (state != State.READ_END_TOKEN) {
            when (state) {
                State.READ_NOTHING -> onReadNothing()
                State.READ_BEGINNING_TOKEN -> onReadBeginningToken()
                State.READ_VALUE -> onReadBeginningToken()
                State.READ_END_TOKEN -> {}
            }
        }

        return ListToken(content)
    }

    private fun onReadNothing() {
        val byte = reader.readNextByte() ?: throw generateException("Unexpected end of file.")

        if (byte != listBeginToken) {
            throw generateException("The list node should start fom the 'l' character.")
        }

        state = State.READ_BEGINNING_TOKEN
    }

    private fun onReadBeginningToken() {
        val nextByte = reader.seeNextByte() ?: throw generateException("Unexpected end of file.")
        if (nextByte == endToken) {
            reader.readNextByte()
            state = State.READ_END_TOKEN

            return
        }

        val tokenizer = buildTokenizerForNextNode(reader)
        content.add(tokenizer.tokenize())
        state = State.READ_VALUE
    }
}