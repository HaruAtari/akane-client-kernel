package com.haruatari.akane.client.kernel.bencode.decoders

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.dto.IntNode
import com.haruatari.akane.client.kernel.bencode.dto.ListNode
import com.haruatari.akane.client.kernel.bencode.dto.Node
import com.haruatari.akane.client.kernel.bencode.dto.StringNode
import com.haruatari.akane.client.kernel.bencode.excetions.DecoderException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import java.io.ByteArrayInputStream

class ListNodeDecoderTest : ExpectSpec({
    context("decode - success") {
        withData(
            mapOf(
                "empty list" to row("le", emptyList<Node>()),
                "list of strings" to row(
                    "l4:test4:teste", listOf(
                        StringNode(byteArrayOf(116, 101, 115, 116)),
                        StringNode(byteArrayOf(116, 101, 115, 116)),
                    )
                ),
                "list of integers" to row(
                    "li11ei12ee", listOf(
                        IntNode(byteArrayOf(49, 49)),
                        IntNode(byteArrayOf(49, 50)),
                    )
                ),
                "mixed list" to row(
                    "l4:testi12ee", listOf(
                        StringNode(byteArrayOf(116, 101, 115, 116)),
                        IntNode(byteArrayOf(49, 50)),
                    )
                ),
                "nested list" to row(
                    "ll4:testee", listOf(
                        ListNode(
                            listOf(
                                StringNode(byteArrayOf(116, 101, 115, 116)),
                            )
                        )
                    )
                ),
            )
        ) { (raw: String, expected: List<Node>) ->
            generateDecoder(raw).decode().getValue() shouldBe expected
        }
    }

    context("decode - fail") {
        withData(
            mapOf(
                "invalid beginning token" to row("a"),
                "unexpected EOF" to row("l"),
                "invalid nested node" to row("l123e"),
            )
        ) { (raw: String) ->
            shouldThrow<DecoderException> {
                generateDecoder(raw).decode()
            }
        }
    }
})

private fun generateDecoder(rawData: String) = ListNodeDecoder(
    Reader(
        ByteArrayInputStream(
            rawData.toByteArray()
        )
    )
)
