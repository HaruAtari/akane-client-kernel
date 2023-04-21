package com.haruatari.akane.client.kernel.storage

import io.kotest.core.spec.style.ExpectSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class StorageTest : ExpectSpec({
    lateinit var storage: Storage
    lateinit var file1: File
    lateinit var file2: File
    lateinit var file3: File

    beforeTest {
        file1 = mockk<File>(relaxed = true)
        every { file1.length } returns 5

        file2 = mockk<File>(relaxed = true)
        every { file2.length } returns 5

        file3 = mockk<File>(relaxed = true)
        every { file3.length } returns 5

        storage = Storage("", listOf(file1, file2, file3))
    }

    context("read") {
        expect("requested slice is inside the first file") {
            storage.read(1, 3)
            verify { file1.read(1, 3) }
            verify(exactly = 0) { file2.read(any(), any()) }
            verify(exactly = 0) { file3.read(any(), any()) }
        }
        expect("requested slice is inside the single file") {
            storage.read(6, 3)
            verify(exactly = 0) { file1.read(any(), any()) }
            verify { file2.read(1, 3) }
            verify(exactly = 0) { file3.read(any(), any()) }
        }
        expect("request zero-length slice") {
            storage.read(6, 0)
            verify(exactly = 0) { file1.read(any(), any()) }
            verify(exactly = 0) { file2.read(any(), any()) }
            verify(exactly = 0) { file3.read(any(), any()) }
        }
        expect("requested slice is the whole file") {
            storage.read(5, 5)
            verify(exactly = 0) { file1.read(any(), any()) }
            verify { file2.read(0, 5) }
            verify(exactly = 0) { file3.read(any(), any()) }
        }
        expect("requested slice intersects two sibling files") {
            storage.read(1, 6)
            verify { file1.read(1, 4) }
            verify { file2.read(0, 2) }
            verify(exactly = 0) { file3.read(any(), any()) }
        }
        expect("requested slice intersects three sibling files") {
            storage.read(2, 10)
            verify { file1.read(2, 3) }
            verify { file2.read(0, 5) }
            verify { file3.read(0, 2) }
        }
    }

    context("write") {
        expect("requested slice is inside the first file") {
            storage.write(1, byteArrayOf(100, 101, 102))
            verify { file1.write(1, byteArrayOf(100, 101, 102)) }
            verify(exactly = 0) { file2.write(any(), any()) }
            verify(exactly = 0) { file3.write(any(), any()) }
        }
        expect("requested slice is inside the single file") {
            storage.write(6, byteArrayOf(100, 101, 102))
            verify(exactly = 0) { file1.write(any(), any()) }
            verify { file2.write(1, byteArrayOf(100, 101, 102)) }
            verify(exactly = 0) { file3.write(any(), any()) }
        }
        expect("request zero-length slice") {
            storage.write(6, byteArrayOf())
            verify(exactly = 0) { file1.write(any(), any()) }
            verify(exactly = 0) { file2.write(any(), any()) }
            verify(exactly = 0) { file3.write(any(), any()) }
        }
        expect("requested slice is the whole file") {
            storage.write(5, byteArrayOf(100, 101, 102, 103, 104))
            verify(exactly = 0) { file1.write(any(), any()) }
            verify { file2.write(0, byteArrayOf(100, 101, 102, 103, 104)) }
            verify(exactly = 0) { file3.write(any(), any()) }
        }
        expect("requested slice intersects two sibling files") {
            storage.write(1, byteArrayOf(100, 101, 102, 103, 104, 105))
            verify { file1.write(1, byteArrayOf(100, 101, 102, 103)) }
            verify { file2.write(0, byteArrayOf(104, 105)) }
            verify(exactly = 0) { file3.write(any(), any()) }
        }
        expect("requested slice intersects three sibling files") {
            storage.write(2, byteArrayOf(100, 101, 102, 103, 104, 105, 106, 107, 108, 109))
            verify { file1.write(2, byteArrayOf(100, 101, 102)) }
            verify { file2.write(0, byteArrayOf(103, 104, 105, 106, 107)) }
            verify { file3.write(0, byteArrayOf(108, 109)) }
        }
    }
})
