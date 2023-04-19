package com.haruatari.akane.client.kernel.bencode.decoder

import com.haruatari.akane.client.kernel.bencode.Reader
import com.haruatari.akane.client.kernel.bencode.dto.announce.Tracker
import com.haruatari.akane.client.kernel.bencode.dto.announce.Peer
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe

class AnnounceDecoderTest : ExpectSpec({
    expect("response with a failed reason") {
        val response = "d14:failure reason11:Test reasone";

        decodeContent(response) shouldBe Tracker(
            failureReason = "Test reason",
            interval = null,
            peers = emptyList()
        )
    }

    expect("response with an empty failed reason") {
        val response = "d14:failure reason0:e";

        decodeContent(response) shouldBe Tracker(
            failureReason = "",
            interval = null,
            peers = emptyList()
        )
    }

    expect("response with a not compact list of peers") {
        val response = "d8:intervali900e5:peersld2:ip13:78.114.69.1934:porti46297eed2:ip13:45.100.69.1934:porti46297e2:id7:Test ideee";
        decodeContent(response) shouldBe Tracker(
            failureReason = null,
            interval = 900,
            peers = listOf(
                Peer(ip = "78.114.69.193", port = 46297, id = null),
                Peer(ip = "45.100.69.193", port = 46297, id = "Test id")
            )
        )
    }
})

private fun decodeContent(content: String): Tracker {
    val decoder = AnnounceTrackerDecoder(content.byteInputStream())

    return decoder.decode()
}