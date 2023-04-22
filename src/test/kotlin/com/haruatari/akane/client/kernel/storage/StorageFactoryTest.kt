package com.haruatari.akane.client.kernel.storage

import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.Info
import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.MetaInfo
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.nio.file.Paths
import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.File as MetaInfoFile

class StorageFactoryTest : ExpectSpec({

    context("buildFromMetaInfo") {
        lateinit var metaInfoMock: MetaInfo

        beforeTest {
            metaInfoMock = mockk<MetaInfo>(relaxed = true)
        }

        expect("single file") {
            val rootPath = "Root path";
            val fileSize = 150L;
            val fileName = "Test file";

            val infoMock = mockk<Info>(relaxed = true);
            every { infoMock.length } returns fileSize
            every { infoMock.name } returns fileName
            every { metaInfoMock.info } returns infoMock

            val actual = StorageFactory().buildFromMetaInfo(rootPath, metaInfoMock)
            val expected = Storage(
                listOf(
                    File(Paths.get(rootPath, fileName), fileSize)
                )
            )

            actual shouldBe expected
        }

        expect("multiple files") {
            val rootPath = "Root path";
            val fileSize1 = 150L;
            val fileName1 = "Test file";
            val fileSize2 = 300L;
            val fileName2 = "Test file2";

            val infoMock = mockk<Info>(relaxed = true);
            every { infoMock.length } returns null
            every { infoMock.files } returns arrayOf(
                MetaInfoFile(arrayOf(rootPath, fileName1), fileSize1),
                MetaInfoFile(arrayOf(rootPath, fileName2), fileSize2)
            )
            every { metaInfoMock.info } returns infoMock

            val actual = StorageFactory().buildFromMetaInfo(rootPath, metaInfoMock)
            val expected = Storage(
                listOf(
                    File(Paths.get(rootPath, fileName1), fileSize1),
                    File(Paths.get(rootPath, fileName2), fileSize2)
                )
            )

            actual shouldBe expected
        }
    }
})