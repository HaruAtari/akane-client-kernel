package com.haruatari.akane.client.kernel.bencode.decoders

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.excetions.DecoderException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import java.io.ByteArrayInputStream

class StringDecoderTest : ExpectSpec({
    context("decode - success") {
        withData(
            mapOf(
                "long string" to row("11:long string", "long string"),
                "short string" to row("4:test", "test"),
                "empty string" to row("0:", ""),
                "with tokens in the value" to row("6:dli10e", "dli10e")
            )
        ) { (raw: String, expected: String) ->
            generateDecoder(raw).decode().getValue() shouldBe expected
        }
    }

    context("decode - fail") {
        withData(
            mapOf(
                "unexpected EOF in the length" to row("4"),
                "unexpected EOF in the value" to row("4:t"),
                "unexpected delimiter" to row(":test"),
                "unexpected char" to row("test"),
                "empty content" to row(""),
            )
        ) { (raw: String) ->
            shouldThrow<DecoderException> {
                generateDecoder(raw).decode()
            }
        }
    }
})

private fun generateDecoder(rawData: String) = StringDecoder(
    Reader(
        ByteArrayInputStream(
            rawData.toByteArray()
        )
    )
)
