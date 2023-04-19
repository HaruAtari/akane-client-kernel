package com.haruatari.akane.client.kernel.bencode.decoders

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.dto.ListNode
import com.haruatari.akane.client.kernel.bencode.dto.Node

internal class ListDecoder(reader: Reader) : NodeDecoder(reader) {
    private enum class State {
        READ_NOTHING,
        READ_BEGINNING_TOKEN,
        READ_VALUE,
        READ_END_TOKEN
    }

    private var state = State.READ_NOTHING
    private var content = mutableListOf<Node>()

    override fun decode(): ListNode {
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

        return ListNode(content)
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

        val decoder = buildDecoderForNextNode(reader)
        content.add(decoder.decode())
        state = State.READ_VALUE
    }
}