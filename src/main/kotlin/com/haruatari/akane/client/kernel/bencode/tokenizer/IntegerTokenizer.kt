package com.haruatari.akane.client.kernel.bencode.tokenizer

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.SpecialSymbols
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.IntegerToken

internal class IntegerTokenizer(reader: Reader) : Tokenizer(reader) {
    private enum class State {
        READ_NOTHING,
        READ_BEGINNING_TOKEN,
        READ_MINUS,
        READ_LEAD_ZERO,
        READ_NUMBER,
        READ_END_TOKEN
    }

    private var content = mutableListOf<Byte>()
    private var state = State.READ_NOTHING
    private var initPosition = 0

    override fun tokenize(): IntegerToken {
        content = mutableListOf()
        state = State.READ_NOTHING
        initPosition = reader.getPosition()

        while (state != State.READ_END_TOKEN) {
            when (state) {
                State.READ_NOTHING -> onReadNothing()
                State.READ_BEGINNING_TOKEN -> onReadBeginningToken()
                State.READ_MINUS -> onReadMinus()
                State.READ_LEAD_ZERO -> onReadLeadZero()
                State.READ_NUMBER -> onReadNumber()
                State.READ_END_TOKEN -> {}
            }
        }

        return IntegerToken(content.toByteArray(), initPosition, reader.getPosition() - 1)
    }

    private fun onReadNothing() {
        val byte = reader.readNextByte() ?: throw generateException("Unexpected end of file.")

        if (byte != SpecialSymbols.intBeginToken) {
            throw generateException("The number node should start fom the 'i' character.")
        }

        state = State.READ_BEGINNING_TOKEN
    }

    private fun onReadBeginningToken() {
        val byte = reader.readNextByte() ?: throw generateException("Unexpected end of file.")

        when (byte) {
            SpecialSymbols.minusToken -> {
                content.add(byte)
                state = State.READ_MINUS

                return
            }

            SpecialSymbols.numberTokens[0] -> {
                content.add(byte)
                state = State.READ_LEAD_ZERO

                return
            }

            in SpecialSymbols.numberTokens -> {
                content.add(byte)
                state = State.READ_NUMBER

                return
            }
        }

        throw generateException("Unexpected char in the number node.")
    }

    private fun onReadMinus() {
        val byte = reader.readNextByte() ?: throw generateException("Unexpected end of file.")

        if (byte in SpecialSymbols.numberTokens && byte != SpecialSymbols.numberTokens[0]) {
            content.add(byte)
            state = State.READ_NUMBER

            return
        }

        throw generateException("Unexpected char in the number node.")
    }

    private fun onReadLeadZero() {
        val byte = reader.readNextByte() ?: throw generateException("Unexpected end of file.")

        if (byte == SpecialSymbols.endToken) {
            state = State.READ_END_TOKEN

            return
        }

        throw generateException("Unexpected char in the number node.")
    }

    private fun onReadNumber() {
        val byte = reader.readNextByte() ?: throw generateException("Unexpected end of file.")

        when (byte) {
            SpecialSymbols.endToken -> {
                state = State.READ_END_TOKEN

                return
            }

            in SpecialSymbols.numberTokens -> {
                content.add(byte)

                return
            }
        }

        throw generateException("Unexpected char in the number node.")
    }
}