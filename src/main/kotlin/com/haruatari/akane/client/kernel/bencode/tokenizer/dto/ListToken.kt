package com.haruatari.akane.client.kernel.bencode.tokenizer.dto

internal class ListToken(private val content: List<Token>) : ContainerToken() {
    override fun getValue(): List<Token> = content
}