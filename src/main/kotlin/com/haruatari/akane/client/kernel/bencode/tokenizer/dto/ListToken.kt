package com.haruatari.akane.client.kernel.bencode.tokenizer.dto

internal fun ListToken(value: List<Token>): ListToken {
    return ListToken(value, 0, 0)
}

internal class ListToken(private val content: List<Token>, from: Int, to: Int) : ContainerToken(from, to) {
    override fun getValue(): List<Token> = content

    override fun toString(): String = "ListToken(from: $from; to: $to; size: ${content.size}; value:\n" + content.joinToString("\n") + "\n)"
}