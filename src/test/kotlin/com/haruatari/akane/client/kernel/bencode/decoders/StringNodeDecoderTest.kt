package com.haruatari.akane.client.kernel.bencode.decoders

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.excetions.DecoderException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import java.io.ByteArrayInputStream

class StringNodeDecoderTest : ExpectSpec({
    context("decode - success") {
        withData(
            mapOf(
                "long string" to row("11:long string", "long string"),
                "short string" to row("4:test", "test"),
                "empty string" to row("0:", ""),
                "with tokens in the value" to row("6:dli10e", "dli10e")
            )
        ) { (raw: String, expected: String) ->
            val stream = ByteArrayInputStream(raw.toByteArray())
            val decoder = StringNodeDecoder(Reader(stream))
            decoder.decode().getValue() shouldBe expected
        }
    }

    context("decode - fail") {
        withData(
            mapOf(
                "unexpected EOF in the length" to row("4"),
                "unexpected EOF in the value" to row("4:t"),
                "unexpected delimiter" to row(":test"),
                "empty content" to row(""),
            )
        ) { (raw: String) ->
            val stream = ByteArrayInputStream(raw.toByteArray())
            val decoder = StringNodeDecoder(Reader(stream))
            shouldThrow<DecoderException> {
                decoder.decode()
            }
        }
    }
})