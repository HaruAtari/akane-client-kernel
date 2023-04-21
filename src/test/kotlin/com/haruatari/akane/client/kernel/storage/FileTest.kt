package com.haruatari.akane.client.kernel.storage

import io.kotest.core.spec.style.ExpectSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.engine.spec.tempfile
import io.kotest.matchers.shouldBe

class FileTest : ExpectSpec({
    context("read") {
        withData(
            mapOf(
                "zero length" to row(
                    byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                    5L, 0,
                    byteArrayOf()
                ),
                "request inside the file" to row(
                    byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                    3L, 3,
                    byteArrayOf(4, 5, 6)
                ),
                "request the whole file" to row(
                    byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                    0L, 10,
                    byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                )
            )
        ) { (fileData: ByteArray, offset: Long, length: Int, expected: ByteArray) ->

            val file = tempfile()
            file.writeBytes(fileData)
            File(file.path, fileData.size.toLong()).read(offset, length) shouldBe expected
        }
    }

    context("write") {
        withData(
            mapOf(
                "zero length" to row(
                    byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
                    5L,
                    byteArrayOf(),
                    byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
                ),
                "write inside the file" to row(
                    byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
                    3L,
                    byteArrayOf(4, 5, 6),
                    byteArrayOf(0, 0, 0, 4, 5, 6, 0, 0, 0, 0),
                ),
                "write the whole file" to row(
                    byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
                    0L,
                    byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                    byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                )
            )
        ) { (fileData: ByteArray, offset: Long, writingData: ByteArray, expected: ByteArray) ->
            val file = tempfile()
            file.writeBytes(fileData)

            val storageFile = File(file.path, fileData.size.toLong())
            storageFile.write(offset, writingData)

            file.readBytes() shouldBe expected
        }
    }
})