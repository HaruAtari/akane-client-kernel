package com.haruatari.akane.client.kernel.bencode.decoders

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.dto.IntNode

internal class IntNodeDecoder(reader: Reader) : NodeDecoder<IntNode>(reader) {
    private enum class State {
        READ_NOTHING,
        READ_BEGINNING_TOKEN,
        READ_MINUS,
        READ_LEAD_ZERO,
        READ_NUMBER,
        READ_END_TOKEN
    }

    private val beginningToken: Byte = 105 // i
    private val endToken: Byte = 101 // e
    private val minusToken: Byte = 45 // e
    private val zeroNumber: Byte = 48
    private val numbers = byteArrayOf(48, 49, 50, 51, 52, 53, 54, 55, 56, 57)
    private var content = mutableListOf<Byte>()
    private var state = State.READ_NOTHING

    override fun decode(): IntNode {
        while (state != State.READ_END_TOKEN) {
            tick()
        }

        return IntNode(content.toByteArray())
    }

    private fun tick() {
        when (state) {
            State.READ_NOTHING -> onReadNothing()
            State.READ_BEGINNING_TOKEN -> onReadBeginningToken()
            State.READ_MINUS -> onReadMinus()
            State.READ_LEAD_ZERO -> onReadLeadZero()
            State.READ_NUMBER -> onReadNumber()
            State.READ_END_TOKEN -> {}
        }
    }

    private fun onReadNothing() {
        val byte = reader.readNextByte() ?: throw generateException("Unexpected end of file.")

        if (byte != beginningToken) {
            throw generateException("The number node should start fom the 'i' character.")
        }

        state = State.READ_BEGINNING_TOKEN
    }

    private fun onReadBeginningToken() {
        val byte = reader.readNextByte() ?: throw generateException("Unexpected end of file.")

        when (byte) {
            minusToken -> {
                content.add(byte)
                state = State.READ_MINUS

                return
            }

            zeroNumber -> {
                content.add(byte)
                state = State.READ_LEAD_ZERO

                return
            }

            in numbers -> {
                content.add(byte)
                state = State.READ_NUMBER

                return
            }
        }

        throw generateException("Unexpected char in the number node.")
    }

    private fun onReadMinus() {
        val byte = reader.readNextByte() ?: throw generateException("Unexpected end of file.")

        if (byte in numbers && byte != minusToken) {
            content.add(byte)
            state = State.READ_NUMBER

            return
        }

        throw generateException("Unexpected char in the number node.")
    }

    private fun onReadLeadZero() {
        val byte = reader.readNextByte() ?: throw generateException("Unexpected end of file.")

        if (byte == endToken) {
            state = State.READ_END_TOKEN

            return
        }

        throw generateException("Unexpected char in the number node.")
    }

    private fun onReadNumber() {
        val byte = reader.readNextByte() ?: throw generateException("Unexpected end of file.")

        when (byte) {
            endToken -> {
                state = State.READ_END_TOKEN

                return
            }

            in numbers -> {
                content.add(byte)

                return
            }
        }

        throw generateException("Unexpected char in the number node.")
    }
}