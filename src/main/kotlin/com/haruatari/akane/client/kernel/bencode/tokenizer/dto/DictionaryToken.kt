package com.haruatari.akane.client.kernel.bencode.tokenizer.dto

internal fun DictionaryToken(value: Map<String, Token>): DictionaryToken {
    return DictionaryToken(value, 0, 0)
}

internal class DictionaryToken(private val content: Map<String, Token>, from: Int, to: Int) : ContainerToken(from, to) {
    override fun getValue(): Map<String, Token> = content

    override fun toString(): String {
        var result = "DictionaryToken(from: $from; to: $to; size: ${content.size}; value:\n"
        for ((key, value) in content) {
            result += "$key: $value,\n"
        }
        result += ")"

        return result
    }
}