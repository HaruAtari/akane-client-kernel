package com.haruatari.akane.client.kernel.bencode.boxer

import com.haruatari.akane.client.kernel.bencode.SpecialSymbols
import com.haruatari.akane.client.kernel.bencode.excetions.BoxerException
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.StringToken
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.Token

internal class StringBoxer : Boxer() {
    override fun box(token: Token): ByteArray {
        if (token !is StringToken) {
            throw BoxerException("StringBoxer can work with StringTokens only.")
        }

        return byteArrayOf(
            *token.getValue().length.toString().toByteArray(Charsets.UTF_8),
            SpecialSymbols.stringDelimiterToken,
            *token.getRawContent()
        );
    }
}