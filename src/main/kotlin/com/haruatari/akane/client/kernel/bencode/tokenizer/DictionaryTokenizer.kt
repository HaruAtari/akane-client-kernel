package com.haruatari.akane.client.kernel.bencode.tokenizer

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.SpecialSymbols
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.DictionaryToken
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.Token

internal class DictionaryTokenizer(reader: Reader) : Tokenizer(reader) {
    private enum class State {
        READ_NOTHING,
        READ_BEGINNING_TOKEN,
        READ_KEY,
        READ_VALUE,
        READ_END_TOKEN
    }

    private var state = State.READ_NOTHING
    private var content = mutableMapOf<String, Token>()
    private var lastKey: String? = null

    override fun tokenize(): DictionaryToken {
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

        return DictionaryToken(content)
    }

    private fun onReadNothing() {
        val byte = reader.readNextByte() ?: throw generateException("Unexpected end of file.")

        if (byte != SpecialSymbols.dictionaryBeginToken) {
            throw generateException("The dictionary node should start fom the 'd' character.")
        }

        state = State.READ_BEGINNING_TOKEN
    }

    private fun onReadBeginningToken() {
        val nextByte = reader.seeNextByte() ?: throw generateException("Unexpected end of file.")
        if (nextByte == SpecialSymbols.endToken) {
            reader.readNextByte()
            state = State.READ_END_TOKEN

            return
        }

        val tokenizer = buildTokenizerForNextNode(reader)
        if (tokenizer !is StringTokenizer) {
            throw generateException("Unexpected character. String node (dictionary key) is expected.")
        }

        lastKey = tokenizer.tokenize().getValue()
        state = State.READ_KEY
    }

    private fun onReadKey() {
        val value = buildTokenizerForNextNode(reader).tokenize()
        content[lastKey!!] = value
        lastKey = null;
        state = State.READ_VALUE
    }
}