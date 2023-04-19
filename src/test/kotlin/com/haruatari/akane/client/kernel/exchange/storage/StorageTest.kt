package com.haruatari.akane.client.kernel.exchange.storage

import io.kotest.core.spec.style.ExpectSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.shouldBe
import java.io.File

class StorageTest : ExpectSpec({
    fun initStorageMock(): StorageMock {
        val dir = tempdir()
        dir.createNewFile()

        val file1 = File(dir.absolutePath + File.separator + "file1")
        file1.writeBytes(byteArrayOf(1, 2, 3, 4, 5))

        val file2 = File(dir.absolutePath + File.separator + "file2")
        file2.writeBytes(byteArrayOf(6, 7, 8, 9, 10))

        val file3 = File(dir.absolutePath + File.separator + "file3")
        file3.writeBytes(byteArrayOf(11, 12, 13, 14, 15))

        return StorageMock(
            Storage(
                dir.toPath(), listOf(
                    Storage.File("file1", file1.length()),
                    Storage.File("file2", file2.length()),
                    Storage.File("file3", file3.length()),
                )
            ),
            file1,
            file2,
            file3
        )
    }

    context("read") {
        withData(
            mapOf(
                "requested slice is inside the first file" to row(1, 3, byteArrayOf(2, 3, 4)),
                "requested slice is inside the single file" to row(6, 3, byteArrayOf(7, 8, 9)),
                "request zero-length slice" to row(6, 0, byteArrayOf()),
                "requested slice is the whole file" to row(5, 5, byteArrayOf(6, 7, 8, 9, 10)),
                "requested slice intersects two sibling files" to row(1, 6, byteArrayOf(2, 3, 4, 5, 6, 7)),
                "requested slice intersects three sibling files" to row(2, 10, byteArrayOf(3, 4, 5, 6, 7, 8, 9, 10, 11, 12)),
            )
        ) { (offset, length, expected) ->
            val storage = initStorageMock().storage
            val actual = storage.read(offset, length)
            actual shouldBe expected
        }
    }

    context("write") {
        withData(
            mapOf(
                "requested slice is inside the first file" to row(
                    1, byteArrayOf(100, 101, 102),
                    listOf(
                        byteArrayOf(1, 100, 101, 102, 5),
                        byteArrayOf(6, 7, 8, 9, 10),
                        byteArrayOf(11, 12, 13, 14, 15)
                    )
                ),
                "requested slice is inside the single file" to row(
                    6, byteArrayOf(100, 101, 102),
                    listOf(
                        byteArrayOf(1, 2, 3, 4, 5),
                        byteArrayOf(6, 100, 101, 102, 10),
                        byteArrayOf(11, 12, 13, 14, 15)
                    )
                ),
                "request zero-length slice" to row(
                    6, byteArrayOf(),
                    listOf(
                        byteArrayOf(1, 2, 3, 4, 5),
                        byteArrayOf(6, 7, 8, 9, 10),
                        byteArrayOf(11, 12, 13, 14, 15)
                    )
                ),
                "requested slice is the whole file" to row(
                    5, byteArrayOf(100, 101, 102, 103, 104),
                    listOf(
                        byteArrayOf(1, 2, 3, 4, 5),
                        byteArrayOf(100, 101, 102, 103, 104),
                        byteArrayOf(11, 12, 13, 14, 15)
                    )
                ),
                "requested slice intersects two sibling files" to row(
                    1, byteArrayOf(100, 101, 102, 103, 104, 105),
                    listOf(
                        byteArrayOf(1, 100, 101, 102, 103),
                        byteArrayOf(104, 105, 8, 9, 10),
                        byteArrayOf(11, 12, 13, 14, 15)
                    )
                ),
                "requested slice intersects three sibling files" to row(
                    2, byteArrayOf(100, 101, 102, 103, 104, 105, 106, 107, 108, 109),
                    listOf(
                        byteArrayOf(1, 2, 100, 101, 102),
                        byteArrayOf(103, 104, 105, 106, 107),
                        byteArrayOf(108, 109, 13, 14, 15)
                    )
                ),
            )
        ) { (offset, writingData, expected) ->
            val storageMock = initStorageMock()
            storageMock.storage.write(offset, writingData)
            storageMock.file1.readBytes() shouldBe expected[0]
            storageMock.file2.readBytes() shouldBe expected[1]
            storageMock.file3.readBytes() shouldBe expected[2]
        }
    }
})

private data class StorageMock(val storage: Storage, val file1: File, val file2: File, val file3: File);