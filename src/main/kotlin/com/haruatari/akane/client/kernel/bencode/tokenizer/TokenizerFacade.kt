package com.haruatari.akane.client.kernel.bencode.tokenizer

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.Token

internal class TokenizerFacade(reader: Reader) : Tokenizer(reader) {
    override fun tokenize(): Token {
        return buildTokenizerForNextNode(reader).tokenize()
    }
}