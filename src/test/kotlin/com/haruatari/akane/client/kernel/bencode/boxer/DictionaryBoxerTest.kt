package com.haruatari.akane.client.kernel.bencode.boxer

import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.DictionaryToken
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.IntegerToken
import com.haruatari.akane.client.kernel.bencode.tokenizer.dto.StringToken
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class DictionaryBoxerTest : ExpectSpec({
    withData(
        mapOf(
            "empty" to row(
                DictionaryToken(emptyMap()),
                byteArrayOf(100, 101)
            ),
            "with integer" to row(
                DictionaryToken(mapOf("test" to IntegerToken(10))),
                byteArrayOf(100, 52, 58, 116, 101, 115, 116, 105, 49, 48, 101, 101)
            ),
            "with string" to row(
                DictionaryToken(mapOf("test" to StringToken("test"))),
                byteArrayOf(100, 52, 58, 116, 101, 115, 116, 52, 58, 116, 101, 115, 116, 101)
            ),
            "nested dictionary" to row(
                DictionaryToken(mapOf("test" to DictionaryToken(emptyMap()))),
                byteArrayOf(100, 52, 58, 116, 101, 115, 116, 100, 101, 101)
            ),
            "unsorted dictionary" to row(
                DictionaryToken(
                    mapOf(
                        "b" to IntegerToken(2),
                        "a" to IntegerToken(1)
                    )
                ),
                byteArrayOf(100, 49, 58, 97, 105, 49, 101, 49, 58, 98, 105, 50, 101, 101)
            )
        )
    ) { (token: DictionaryToken, expected: ByteArray) ->
        val actual = DictionaryBoxer().box(token)
        actual shouldBe expected
    }
})