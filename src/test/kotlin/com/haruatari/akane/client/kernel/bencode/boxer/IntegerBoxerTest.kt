package com.haruatari.akane.client.kernel.bencode.boxer

import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.NumberToken
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class IntegerBoxerTest : ExpectSpec({
    withData(
        mapOf(
            "positive value" to row(
                NumberToken(10),
                byteArrayOf(105, 49, 48, 101)
            ),
            "negative value" to row(
                NumberToken(-10),
                byteArrayOf(105, 45, 49, 48, 101)
            ),
            "zero value" to row(
                NumberToken(0),
                byteArrayOf(105, 48, 101)
            ),
        )
    ) { (token: NumberToken, expected: ByteArray) ->
        val actual = IntegerBoxer().box(token)
        actual shouldBe expected
    }
})