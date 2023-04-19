package com.haruatari.akane.client.kernel.bencode.encoder

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.decoder.MetaInfoDecoder
import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.File
import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.Info
import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.MetaInfo
import com.haruatari.akane.client.kernel.bencode.dto.metaInfo.Piece
import io.kotest.core.spec.Order
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe

@Order(1) // After decoder
class MetaInfoEncoderTest : ExpectSpec({
    expect("single file") {
        val expected = MetaInfo(
            announce = "udp://tracker.openbittorrent.com:8013",
            info = Info(
                name = "image.jpg",
                pieceLength = 65536,
                pieces = arrayOf(
                    Piece("8fb5be7664e10ad5161053c23bd3dcc8625d2181"),
                    Piece("31bd1b08b2df4ef77510fbaacb3307af7e874e2e"),
                    Piece("947b084dbc39e7f5f62dbfe70bc0739113778ba6"),
                    Piece("bf74f6ecac52018eeeb97f9f7f26a8a6d4c4d969"),
                    Piece("8aa0c895387fe51ddf9550fcc887f062f1dc81a5"),
                    Piece("b4fe1e4d0a8ce9c0783edc6258cc7858dd327a4e"),
                    Piece("7223022d623737f7078b316bf5b435c37f6ee83c"),
                    Piece("23ec6ddd40169bca997b554b627d0f1fc094d242"),
                    Piece("a6fc9cb47a9eefec0468d53b2fce085dd3f836b1"),
                    Piece("78db6295f9f7d403a2fd1dbf8d3d277483c3955c"),
                ),
                length = 633493,
                files = emptyArray()
            )
        )
        val actual = MetaInfoDecoder(Reader(MetaInfoEncoder().encode(expected))).decode()
        actual shouldBe expected
    }

    expect("several files") {
        val expected = MetaInfo(
            announce = "udp://tracker.openbittorrent.com:8013",
            info = Info(
                name = "New folder",
                pieceLength = 65536,
                pieces = arrayOf(
                    Piece("8fb5be7664e10ad5161053c23bd3dcc8625d2181"),
                    Piece("31bd1b08b2df4ef77510fbaacb3307af7e874e2e"),
                    Piece("947b084dbc39e7f5f62dbfe70bc0739113778ba6"),
                    Piece("bf74f6ecac52018eeeb97f9f7f26a8a6d4c4d969"),
                    Piece("8aa0c895387fe51ddf9550fcc887f062f1dc81a5"),
                    Piece("b4fe1e4d0a8ce9c0783edc6258cc7858dd327a4e"),
                    Piece("7223022d623737f7078b316bf5b435c37f6ee83c"),
                    Piece("23ec6ddd40169bca997b554b627d0f1fc094d242"),
                    Piece("a6fc9cb47a9eefec0468d53b2fce085dd3f836b1"),
                    Piece("5597af9694462be768d733b9d47b5a774edc2b81"),
                    Piece("e9c71ebdb829f46351aae0dd877c579667351f82"),
                ),
                length = null,
                files = arrayOf(
                    File(
                        path = arrayOf("image1.jpg"),
                        length = 633493
                    ),
                    File(
                        path = arrayOf("dir1", "image2.jpg"),
                        length = 32503
                    ),
                    File(
                        path = arrayOf("dir1", "dir2", "image3.jpg"),
                        length = 32503
                    )
                )
            )
        )
        val actual = MetaInfoDecoder(Reader(MetaInfoEncoder().encode(expected))).decode()
        actual shouldBe expected
    }
})