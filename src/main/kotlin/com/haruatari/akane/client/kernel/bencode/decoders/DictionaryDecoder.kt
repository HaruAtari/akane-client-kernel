package com.haruatari.akane.client.kernel.bencode.decoders

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.dto.DictionaryNode
import com.haruatari.akane.client.kernel.bencode.dto.Node

internal class DictionaryDecoder(reader: Reader) : NodeDecoder(reader) {
    private enum class State {
        READ_NOTHING,
        READ_BEGINNING_TOKEN,
        READ_KEY,
        READ_VALUE,
        READ_END_TOKEN
    }

    private var state = State.READ_NOTHING
    private var content = mutableMapOf<String, Node>()
    private var lastKey: String? = null

    override fun decode(): DictionaryNode {
        content = mutableMapOf()
        state = State.READ_NOTHING
        lastKey = null

        while (state != State.READ_END_TOKEN) {
            when (state) {
                State.READ_NOTHING -> onReadNothing()
                State.READ_BEGINNING_TOKEN -> onReadBeginningToken()
                State.READ_KEY -> onReadKey()
                State.READ_VALUE -> onReadBeginningToken()
                State.READ_END_TOKEN -> {}
            }
        }

        return DictionaryNode(content)
    }

    private fun onReadNothing() {
        val byte = reader.readNextByte() ?: throw generateException("Unexpected end of file.")

        if (byte != dictionaryBeginToken) {
            throw generateException("The dictionary node should start fom the 'd' character.")
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

        val decoder = buildDecoderForNextNode(reader)
        if (decoder !is StringDecoder) {
            throw generateException("Unexpected character. String node (dictionary key) is expected.")
        }

        lastKey = decoder.decode().getValue()
        state = State.READ_KEY
    }

    private fun onReadKey() {
        val value = buildDecoderForNextNode(reader).decode()
        content[lastKey!!] = value
        lastKey = null;
        state = State.READ_VALUE
    }
}