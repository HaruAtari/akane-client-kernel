package com.haruatari.akane.client.kernel.bencode.tokenizer

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.excetions.TokenizerException
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.DictionaryToken
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.IntegerToken
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.StringToken
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.Token
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import java.io.ByteArrayInputStream

class DictionaryTokenizerTest : ExpectSpec({
    context("tokenize - success") {
        withData(
            mapOf(
                "empty dictionary" to row("de", emptyMap<String, Token>()),
                "plain dictionary" to row(
                    "d4:key14:test4:key2i12ee", mapOf(
                        "key1" to StringToken(byteArrayOf(116, 101, 115, 116)),
                        "key2" to IntegerToken(byteArrayOf(49, 50)),
                    )
                ),
                "nested dictionary" to row(
                    "d4:key1d4:key24:testee", mapOf(
                        "key1" to DictionaryToken(
                            mapOf(
                                "key2" to StringToken(byteArrayOf(116, 101, 115, 116)),
                            )
                        ),
                    )
                ),
            )
        ) { (raw: String, expected: Map<String, Token>) ->
            generateTokenizer(raw).tokenize().getValue() shouldBe expected
        }
    }

    context("tokenize - fail") {
        withData(
            mapOf(
                "invalid beginning token" to row("a"),
                "unexpected EOF" to row("d"),
                "invalid key node" to row("di12ee"),
                "invalid value node" to row("di12e123e"),
            )
        ) { (raw: String) ->
            shouldThrow<TokenizerException> {
                generateTokenizer(raw).tokenize()
            }
        }
    }
})

private fun generateTokenizer(rawData: String) = DictionaryTokenizer(
    Reader(
        ByteArrayInputStream(
            rawData.toByteArray()
        )
    )
)
