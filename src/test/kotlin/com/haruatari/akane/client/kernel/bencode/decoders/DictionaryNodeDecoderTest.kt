package com.haruatari.akane.client.kernel.bencode.decoders

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.dto.DictionaryNode
import com.haruatari.akane.client.kernel.bencode.dto.IntNode
import com.haruatari.akane.client.kernel.bencode.dto.Node
import com.haruatari.akane.client.kernel.bencode.dto.StringNode
import com.haruatari.akane.client.kernel.bencode.excetions.DecoderException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import java.io.ByteArrayInputStream

class DictionaryNodeDecoderTest : ExpectSpec({
    context("decode - success") {
        withData(
            mapOf(
                "empty dictionary" to row("de", emptyMap<String, Node>()),
                "plain dictionary" to row(
                    "d4:key14:test4:key2i12ee", mapOf(
                        "key1" to StringNode(byteArrayOf(116, 101, 115, 116)),
                        "key2" to IntNode(byteArrayOf(49, 50)),
                    )
                ),
                "nested dictionary" to row(
                    "d4:key1d4:key24:testee", mapOf(
                        "key1" to DictionaryNode(
                            mapOf(
                                "key2" to StringNode(byteArrayOf(116, 101, 115, 116)),
                            )
                        ),
                    )
                ),
            )
        ) { (raw: String, expected: Map<String, Node>) ->
            generateDecoder(raw).decode().getValue() shouldBe expected
        }
    }

    context("decode - fail") {
        withData(
            mapOf(
                "invalid beginning token" to row("a"),
                "unexpected EOF" to row("d"),
                "invalid key node" to row("di12ee"),
                "invalid value node" to row("di12e123e"),
            )
        ) { (raw: String) ->
            shouldThrow<DecoderException> {
                generateDecoder(raw).decode()
            }
        }
    }
})

private fun generateDecoder(rawData: String) = DictionaryNodeDecoder(
    Reader(
        ByteArrayInputStream(
            rawData.toByteArray()
        )
    )
)
