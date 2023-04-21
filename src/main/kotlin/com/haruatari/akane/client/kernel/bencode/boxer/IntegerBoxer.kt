package com.haruatari.akane.client.kernel.bencode.boxer

import com.haruatari.akane.client.kernel.bencode.SpecialSymbols
import com.haruatari.akane.client.kernel.bencode.excetions.BoxerException
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.NumberToken
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.Token

internal class IntegerBoxer : Boxer() {
    override fun box(token: Token): ByteArray {
        if (token !is NumberToken) {
            throw BoxerException("IntegerBoxer can work with IntegerTokens only.")
        }

        return byteArrayOf(
            SpecialSymbols.intBeginToken,
            *token.getRawContent(),
            SpecialSymbols.endToken
        )
    }
}