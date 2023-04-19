package com.haruatari.akane.client.kernel.bencode.tokenizer

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.excetions.TokenizerException
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.IntegerToken
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.ListToken
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.StringToken
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.Token
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import java.io.ByteArrayInputStream

class ListTokenizerTest : ExpectSpec({
    context("tokenize - success") {
        withData(
            mapOf(
                "empty list" to row("le", emptyList<Token>()),
                "list of strings" to row(
                    "l4:test4:teste", listOf(
                        StringToken(byteArrayOf(116, 101, 115, 116)),
                        StringToken(byteArrayOf(116, 101, 115, 116)),
                    )
                ),
                "list of integers" to row(
                    "li11ei12ee", listOf(
                        IntegerToken(byteArrayOf(49, 49)),
                        IntegerToken(byteArrayOf(49, 50)),
                    )
                ),
                "mixed list" to row(
                    "l4:testi12ee", listOf(
                        StringToken(byteArrayOf(116, 101, 115, 116)),
                        IntegerToken(byteArrayOf(49, 50)),
                    )
                ),
                "nested list" to row(
                    "ll4:testee", listOf(
                        ListToken(
                            listOf(
                                StringToken(byteArrayOf(116, 101, 115, 116)),
                            )
                        )
                    )
                ),
            )
        ) { (raw: String, expected: List<Token>) ->
            generateTokenizer(raw).tokenize().getValue() shouldBe expected
        }
    }

    context("tokenize - fail") {
        withData(
            mapOf(
                "invalid beginning token" to row("a"),
                "unexpected EOF" to row("l"),
                "invalid nested node" to row("l123e"),
            )
        ) { (raw: String) ->
            shouldThrow<TokenizerException> {
                generateTokenizer(raw).tokenize()
            }
        }
    }
})

private fun generateTokenizer(rawData: String) = ListTokenizer(
    Reader(
        ByteArrayInputStream(
            rawData.toByteArray()
        )
    )
)
