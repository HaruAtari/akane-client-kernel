package com.haruatari.akane.client.kernel.bencode.boxer

import com.haruatari.akane.client.kernel.bencode.SpecialSymbols
import com.haruatari.akane.client.kernel.bencode.excetions.BoxerException
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.ListToken
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.Token

internal class ListBoxer : Boxer() {
    override fun box(token: Token): ByteArray {
        if (token !is ListToken) {
            throw BoxerException("ListBoxer can work with ListTokens only.")
        }

        return buildList {
            add(SpecialSymbols.listBeginToken)
            for (child in token.getValue()) {
                addAll(getBoxerForToken(child).box(child).toList())
            }
            add(SpecialSymbols.endToken)
        }.toByteArray()
    }
}