package com.haruatari.akane.client.kernel.bencode.boxer

import com.haruatari.akane.client.kernel.bencode.SpecialSymbols
import com.haruatari.akane.client.kernel.bencode.excetions.BoxerException
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.DictionaryToken
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.StringToken
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.Token

internal class DictionaryBoxer : Boxer() {
    override fun box(token: Token): ByteArray {
        if (token !is DictionaryToken) {
            throw BoxerException("DictionaryBoxer can work with DictionaryTokens only.")
        }

        return buildList {
            add(SpecialSymbols.dictionaryBeginToken)
            for (key in token.getValue().keys.sorted()) {
                val keyToken = StringToken(key)
                addAll(getBoxerForToken(keyToken).box(keyToken).toList())

                val valueToken = token.getValue()[key]!!
                addAll(getBoxerForToken(valueToken).box(valueToken).toList())
            }
            add(SpecialSymbols.endToken)
        }.toByteArray()
    }
}