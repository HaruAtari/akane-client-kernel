package com.haruatari.akane.client.kernel.bencode.tokenizer

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.excetions.TokenizerException
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.Token

internal abstract class Tokenizer(protected val reader: Reader) {
    protected val listBeginToken: Byte = 108 // l
    protected val dictionaryBeginToken: Byte = 100; // d
    protected val intBeginToken: Byte = 105; // i
    protected val endToken: Byte = 101 // e
    protected val stringDelimiterToken: Byte = 58 // :
    protected val numberTokens = byteArrayOf(48, 49, 50, 51, 52, 53, 54, 55, 56, 57)
    protected val minusToken: Byte = 45 // -

    abstract fun tokenize(): Token

    protected fun buildTokenizerForNextNode(reader: Reader): Tokenizer {
        val nextByte = reader.seeNextByte() ?: throw generateException("Unexpected end of file.");

        return when (nextByte) {
            listBeginToken -> ListTokenizer(reader)
            dictionaryBeginToken -> DictionaryTokenizer(reader)
            intBeginToken -> IntegerTokenizer(reader)
            in numberTokens -> StringTokenizer(reader)
            else -> throw generateException("Undefined node's first char.")
        }
    }

    protected fun generateException(message: String): TokenizerException {
        return TokenizerException(message + " Position: " + reader.getLastReadingPosition())
    }
}