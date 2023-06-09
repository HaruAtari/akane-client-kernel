package com.haruatari.akane.client.kernel.bencode.boxer

import com.haruatari.akane.client.kernel.bencode.excetions.BoxerException
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.*

internal abstract class Boxer {
    abstract fun box(token: Token): ByteArray

    fun getBoxerForToken(token: Token): Boxer {
        return when (token) {
            is StringToken -> StringBoxer()
            is NumberToken -> IntegerBoxer()
            is ListToken -> ListBoxer()
            is DictionaryToken -> DictionaryBoxer()
            else -> throw BoxerException("Undefined token type")
        }
    }
}

