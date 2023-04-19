package com.haruatari.akane.client.kernel.bencode.tokenizer

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.excetions.TokenizerException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import java.io.ByteArrayInputStream

class StringTokenizerTest : ExpectSpec({
    context("tokenize - success") {
        withData(
            mapOf(
                "long string" to row("11:long string", "long string"),
                "short string" to row("4:test", "test"),
                "empty string" to row("0:", ""),
                "with tokens in the value" to row("6:dli10e", "dli10e")
            )
        ) { (raw: String, expected: String) ->
            generateTokenizer(raw).tokenize().getValue() shouldBe expected
        }
    }

    context("tokenize - fail") {
        withData(
            mapOf(
                "unexpected EOF in the length" to row("4"),
                "unexpected EOF in the value" to row("4:t"),
                "unexpected delimiter" to row(":test"),
                "unexpected char" to row("test"),
                "empty content" to row(""),
            )
        ) { (raw: String) ->
            shouldThrow<TokenizerException> {
                generateTokenizer(raw).tokenize()
            }
        }
    }
})

private fun generateTokenizer(rawData: String) = StringTokenizer(
    Reader(
        ByteArrayInputStream(
            rawData.toByteArray()
        )
    )
)
