package com.haruatari.akane.client.kernel.bencode.tokenizer.dto

internal class DictionaryToken(private val content: Map<String, Token>) : ContainerToken() {
    override fun getValue(): Map<String, Token> = content
}