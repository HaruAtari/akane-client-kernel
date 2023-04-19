package com.haruatari.akane.client.kernel.bencode.boxer

import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.StringToken
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class StringBoxerTest : ExpectSpec({
    withData(
        mapOf(
            "short value" to row(
                StringToken("test"),
                byteArrayOf(52, 58, 116, 101, 115, 116),
                "long value" to row(
                    StringToken("test test test"),
                    byteArrayOf(49, 52, 58, 116, 101, 115, 116, 32, 116, 101, 115, 116, 32, 116, 101, 115, 116)
                ),
                "empty value" to row(
                    StringToken(""),
                    byteArrayOf(48, 58)
                )
            ),
        )
    ) { (token: StringToken, expected: ByteArray) ->
        val actual = StringBoxer().box(token)
        actual shouldBe expected
    }
})