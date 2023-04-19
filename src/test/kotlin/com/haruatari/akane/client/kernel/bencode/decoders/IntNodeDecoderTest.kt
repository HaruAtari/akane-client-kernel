package com.haruatari.akane.client.kernel.bencode.decoders

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.excetions.DecoderException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import java.io.ByteArrayInputStream

class IntNodeDecoderTest : ExpectSpec({
    context("decode - success") {
        withData(
            mapOf(
                "long positive" to row("i1023456789e", 1023456789),
                "short positive" to row("i1e", 1),
                "zero" to row("i0e", 0),
                "long negative" to row("i-1023456789e", -1023456789),
                "short negative" to row("i-1e", -1),
            )
        ) { (raw: String, expected: Int) ->
            generateDecoder(raw).decode().getValue() shouldBe expected
        }
    }

    context("decode - fail") {
        withData(
            mapOf(
                "negative zero" to row("i-0e"),
                "lead zero" to row("i01e"),
                "lead negative zero" to row("i-01e"),
                "without beginning token" to row("10e"),
                "without end token" to row("i10"),
                "unexpected char" to row("i10k10e"),
                "empty content" to row(""),
            )
        ) { (raw: String) ->
            shouldThrow<DecoderException> {
                generateDecoder(raw).decode()
            }
        }
    }
})

private fun generateDecoder(rawData: String) = IntNodeDecoder(
    Reader(
        ByteArrayInputStream(
            rawData.toByteArray()
        )
    )
)