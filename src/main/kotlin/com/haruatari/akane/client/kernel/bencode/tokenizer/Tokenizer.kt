package com.haruatari.akane.client.kernel.bencode.tokenizer

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.SpecialSymbols
import com.haruatari.akane.client.kernel.bencode.excetions.TokenizerException
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.Token

internal abstract class Tokenizer(protected val reader: Reader) {

    abstract fun tokenize(): Token

    protected fun buildTokenizerForNextNode(reader: Reader): Tokenizer {
        val nextByte = reader.seeNextByte() ?: throw generateException("Unexpected end of file.")

        return when (nextByte) {
            SpecialSymbols.listBeginToken -> ListTokenizer(reader)
            SpecialSymbols.dictionaryBeginToken -> DictionaryTokenizer(reader)
            SpecialSymbols.intBeginToken -> IntegerTokenizer(reader)
            in SpecialSymbols.numberTokens -> StringTokenizer(reader)
            else -> throw generateException("Undefined node's first char." + reader.getPosition())
        }
    }

    protected fun generateException(message: String): TokenizerException {
        return TokenizerException(message + " Position: " + reader.getLastReadingPosition())
    }
}