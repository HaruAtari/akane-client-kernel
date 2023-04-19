package com.haruatari.akane.client.kernel.bencode.boxer

import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.IntegerToken
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.ListToken
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.StringToken
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class ListBoxerTest : ExpectSpec({
    withData(
        mapOf(
            "empty" to row(
                ListToken(emptyList()),
                byteArrayOf(108, 101)
            ),
            "with integer" to row(
                ListToken(listOf(IntegerToken(10))),
                byteArrayOf(108, 105, 49, 48, 101, 101)
            ),
            "with string" to row(
                ListToken(listOf(StringToken("test"))),
                byteArrayOf(108, 52, 58, 116, 101, 115, 116, 101)
            ),
            "nested list" to row(
                ListToken(listOf(ListToken(emptyList()))),
                byteArrayOf(108, 108, 101, 101)
            ),
        )
    ) { (token: ListToken, expected: ByteArray) ->
        val actual = ListBoxer().box(token)
        actual shouldBe expected
    }
})